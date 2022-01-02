package com.ariel.readme.data.repo

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.ariel.readme.data.repo.interfaces.IContactRepository


class ContactRepository: IContactRepository {
    override fun getContactName(phoneNumber: String?, context: Context): String? {
        val uri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        var contactName: String? = null
        val cursor: Cursor? = context.getContentResolver().query(uri, projection, null, null, null)
        cursor?.let{
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0)
            }
            cursor.close()
        }

        return contactName
    }
}