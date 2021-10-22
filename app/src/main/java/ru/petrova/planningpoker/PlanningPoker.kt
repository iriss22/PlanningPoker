package ru.petrova.planningpoker

import android.app.Application
import android.util.Log

import io.realm.Realm
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration

lateinit var pokerApp: App

inline fun <reified T> T.TAG(): String = T::class.java.simpleName

class PlaningPoker : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        pokerApp = App(
            AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .build())

        if (BuildConfig.DEBUG) {
            RealmLog.setLevel(LogLevel.ALL)
        }

        Log.v(TAG(), "Initialized the Realm App configuration for: ${pokerApp.configuration.appId}")
    }
}