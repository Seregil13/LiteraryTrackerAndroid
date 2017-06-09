package com.seregil13.literarytracker.lightnovel

import android.os.Bundle
import android.support.v4.app.Fragment
import com.seregil13.literarytracker.realm.LightNovel
import com.seregil13.literarytracker.util.JsonKeys
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_lightnovel_detail.*
import kotlinx.android.synthetic.main.lightnovel_detail.*
import kotlin.properties.Delegates

class LightNovelDetailsFragment: Fragment() {

    private var realm: Realm by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var title: String = ""
        if (arguments.containsKey(JsonKeys.TITLE.toString()))
            title = arguments[JsonKeys.TITLE.toString()] as String

        realm = Realm.getDefaultInstance()
        val ln = realm.where(LightNovel::class.java).equalTo("title", title).findFirst()

        // TODO: pass data back to activity

        author.text = ln.author
        completionStatus.text = if (ln.completed) "Completed" else "In Progress"
        description.text = ln.description
        translatorSite.text = ln.translators[0].name // todo
        genres.removeAllViews()
        genres.addTextViews(ln.genres.map { it.name })

        activity.toolbar_layout.title = ln.title
    }
}