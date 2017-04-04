package xyz.georgihristov.feelngs

import android.app.Application
import kotlin.reflect.KProperty

var prefs: Prefs by lazy{ App.appPrefs!! }

private operator fun Any.setValue(nothing: Nothing?, property: KProperty<*>, prefs: Prefs) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}


class App : Application(){

     companion object{
         var appPrefs: Prefs? = null
     }


    override fun onCreate() {
        appPrefs = Prefs(applicationContext)
        super.onCreate()
    }
}
