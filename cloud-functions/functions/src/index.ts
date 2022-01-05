import * as functions from "firebase-functions";
import * as speech from "@google-cloud/speech";
import * as path from "path";
import {getFirestore} from "firebase-admin/firestore";
import {applicationDefault, initializeApp} from "firebase-admin/app";
import {google} from "@google-cloud/speech/build/protos/protos";
import AudioEncoding = google.cloud.speech.v1.RecognitionConfig.AudioEncoding;


initializeApp({
  credential: applicationDefault(),
  databaseURL: "https://readmenew-2528d.firebaseio.com",
});

const PATH_AUDIO_FILES = "transcriptions/audio-files";

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

  const gcsUri = `gs://readmenew-2528d.appspot.com/${PATH_AUDIO_FILES}/${fileName}`;
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
