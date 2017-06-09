package com.seregil13.literarytracker.realm

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class LightNovel: RealmObject() {
    @PrimaryKey
    var title: String = ""
    var author: String = ""
    var description: String = ""
    var completed: Boolean = false
    var translators: RealmList<Translator> = RealmList()
    var genres: RealmList<Genre> = RealmList()

    override fun toString(): String {
        return "title: $title\nauthor: $author\ndescription: $description\ncompleted: $completed\ntranslator sites: ${translators.joinToString { it.name }}\ngenres: ${genres.joinToString { it.name }}"
    }
}