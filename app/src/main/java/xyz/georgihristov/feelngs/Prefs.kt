package xyz.georgihristov.feelngs

import android.content.Context

class Prefs(val context : Context){

    private val PREFS_FILE = "xyz.georgihristov.feelngs.PREFS_FILE"
    private val AUTH_TOKEN = "xyz.georgihristov.feelngs.AUTH_TOKEN"

    private var preferences = context.getSharedPreferences(PREFS_FILE,Context.MODE_PRIVATE)
    private var editor = preferences.edit()


    var authToken : String
    get() = preferences.getString(AUTH_TOKEN,"")
    set(value) = editor.putString(AUTH_TOKEN,value).apply()

}