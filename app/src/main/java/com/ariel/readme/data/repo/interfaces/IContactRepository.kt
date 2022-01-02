package com.ariel.readme.data.repo.interfaces

import android.content.Context

interface IContactRepository {
    fun getContactName(phoneNumber: String?, context: Context): String?
}