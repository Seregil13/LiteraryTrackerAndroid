package com.seregil13.literarytracker.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Genre: RealmObject() {
    @PrimaryKey
    var name: String = ""
}