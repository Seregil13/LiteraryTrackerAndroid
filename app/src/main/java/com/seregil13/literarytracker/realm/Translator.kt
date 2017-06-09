package com.seregil13.literarytracker.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Translator : RealmObject() {
    @PrimaryKey
    var name: String = ""

    override fun toString(): String {
        return "name: $name"
    }
}