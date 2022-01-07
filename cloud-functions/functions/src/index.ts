import * as functions from "firebase-functions";
import * as speech from "@google-cloud/speech";
import * as path from "path";

import * as firestore from "firebase-admin/firestore";
import {getFirestore} from "firebase-admin/firestore";
import {getMessaging} from "firebase-admin/messaging";
import {applicationDefault, initializeApp} from "firebase-admin/app";
import {google} from "@google-cloud/speech/build/protos/protos";
import AudioEncoding = google.cloud.speech.v1.RecognitionConfig.AudioEncoding;

const PROJECT_ID = "readmenew-2528d";
const PATH_AUDIO_FILES = "transcriptions/audio-files";

initializeApp({
  credential: applicationDefault(),
  projectId: PROJECT_ID,
  // databaseURL: `https://${PROJECT_ID}-2528d.firebaseio.com`,
});

// eslint-disable-next-line max-len
const getRandomInt = (min: number, max: number) => Math.floor(max - Math.random() * min);

// eslint-disable-next-line max-len
exports.collectStats = functions.pubsub.schedule("every 1 minutes").onRun(async (context) => {
  const mpr = getRandomInt(100, 130);
  const activeUsers = getRandomInt(100, 130);

  const db = getFirestore();
  const statsColl = db.collection("statistics");

  const data = {
    mpr: mpr,
    active_users: activeUsers,
    ts: firestore.FieldValue.serverTimestamp(),
  };

  await statsColl.add(data);
  functions.logger.info("Added stats to db!", {structuredData: false});
});


exports.onMessageTextChange = functions.firestore
    .document("messages/{mid}")
    .onWrite(async (change, context) => {
      if (change.before.data()?.text === change.after.data()?.text) {
        return;
      }

      const db = getFirestore();
      const usersColl = db.collection("users");
      const chatColl = db.collection("chats");
      const hotwordsColl = db.collection("hotwords");

      const cid = change.after.data()?.cid;
      const chat = await chatColl.doc(`/${cid}`).get();

      // eslint-disable-next-line max-len
      const participants:Array<string> = chat?.data()?.participants.filter((participant:string) => {
        return participant !== change.after.data()?.from;
      });

      const receiver = participants[0];
      const receiverObj= await usersColl.doc(receiver).get();

      if (receiverObj.data() === undefined) {
        return;
      }

      // Collect hot words for user
      const hotWordsDB = await hotwordsColl.where("uid", "==", receiver).get();
      // eslint-disable-next-line max-len
      const hotWordList = hotWordsDB.docChanges().map( (word) => word.doc.data().word);

      const messageWords = change.after.data()?.text.split(" ");
      // eslint-disable-next-line max-len
      const matchedWords: Array<string> = hotWordList.filter( (hotWord) => messageWords.includes(hotWord));

      if (matchedWords.length == 0) {
        return;
      }

      const message = {
        notification: {
          title: "ALERT ALERT ALERT!",
          body: "Hot word triggered an alert!!!",
        },
        android: {
          notification: {
            channelId: "hotwords",
          },
        },
        token: receiverObj.data()?.token,
      };

      getMessaging().send(message)
          .then((response) => {
          // Response is a message ID string.
            console.log("Successfully sent alert:", response);
          })
          .catch((error) => {
            console.error("Error sending alert:", error);
          });
    });

exports.onNewMessage = functions.firestore
    .document("messages/{mid}")
    .onCreate(async (snap, context) => {
      const db = getFirestore();
      const usersColl = db.collection("users");
      const chatColl = db.collection("chats");

      const cid = snap.data().cid;
      const chat = await chatColl.doc(`/${cid}`).get();
      const participants = chat?.data()?.participants;

      // eslint-disable-next-line max-len
      const users = await usersColl.where(firestore.FieldPath.documentId(), "in", participants).get();
      const notificationBody = {
        title: "New message!",
        body: `in chat ${cid}, click here to open it.`,
      };

      users.forEach(function(user) {
        const message = {
          notification: notificationBody,
          token: user.data().token,
        };

        // Send a message to the device corresponding to the provided
        // registration token.
        getMessaging().send(message)
            .then((response) => {
              // Response is a message ID string.
              console.log("Successfully sent message:", response);
            })
            .catch((error) => {
              console.error("Error sending message:", error);
            });
      });
    });


exports.onNewUser = functions.firestore
    .document("users/{uid}")
    .onCreate(async (snap, context) => {
      const db = getFirestore();
      const collRef = db.collection("users");
      const managers = await collRef.where("manager", "==", true).get();
      if (managers.size != 0) {
        return;
      }
      const newData = snap.data();
      newData["manager"] = true;
      await collRef.doc(snap.id).set(newData);
    });


// eslint-disable-next-line max-len
exports.onNewSoundFile = functions.storage.object().onFinalize(async (object) => {
  if (object == undefined || object.name == undefined) {
    return;
  }

  if (!object.name.startsWith(PATH_AUDIO_FILES)) {
    return;
  }

  const db = getFirestore();
  const client = new speech.SpeechClient();
  const fileName = path.basename(object.name);
  const mId = fileName.split(".")[0];
  const messageRef = db.collection("messages").doc(mId);

  await messageRef.update({transcriptStatus: "RUNNING"});

  const gcsUri = `gs://${PROJECT_ID}.appspot.com/${PATH_AUDIO_FILES}/${fileName}`;
  const audio = {
    uri: gcsUri,
  };
  const config = {
    encoding: AudioEncoding.AMR_WB,
    sampleRateHertz: 16000,
    languageCode: "en-US",
  };
  const request = {
    audio: audio,
    config: config,
  };

  try {
    const [response] = await client.recognize(request);
    if (response.results) {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      // eslint-disable-next-line max-len
      const transcription = response.results.map((result) => result.alternatives[0].transcript).join("\n");
      await messageRef.update({
        text: transcription,
        transcriptStatus: "DONE",
      });
      // eslint-disable-next-line max-len
      functions.logger.info(`Transcription: ${transcription}, ${mId}`, {structuredData: false});
    }
  } catch (e) {
    functions.logger.error(e, {structuredData: true});
    await messageRef.update({
      transcriptStatus: "FAILED",
    });
  }
});
