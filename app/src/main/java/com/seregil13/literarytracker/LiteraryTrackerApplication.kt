package com.seregil13.literarytracker

import android.app.Application
import io.realm.Realm

/**
 * Created by Alec on 6/3/2017.
 */

open class LiteraryTrackerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}