package com.seregil13.literarytracker.lightnovel

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.seregil13.literarytracker.R
import com.seregil13.literarytracker.util.EDIT_REQUEST_CODE
import com.seregil13.literarytracker.util.JsonKeys
import com.seregil13.literarytracker.util.LiteraryTrackerUtils
import com.seregil13.literarytracker.util.REQUEST_CODE_KEY
import kotlinx.android.synthetic.main.activity_lightnovel_detail.*
import java.util.ArrayList

class LightNovelDetailActivity: AppCompatActivity(), OnDataFetched {

    private var realm: io.realm.Realm by kotlin.properties.Delegates.notNull()
    private var mLightNovel: com.seregil13.literarytracker.realm.LightNovel by kotlin.properties.Delegates.notNull()

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_lightnovel_detail)
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        realm = io.realm.Realm.getDefaultInstance()

        realm.executeTransaction {
            mLightNovel = realm.where(com.seregil13.literarytracker.realm.LightNovel::class.java)
                    .equalTo("title", intent.getStringExtra(com.seregil13.literarytracker.util.JsonKeys.TITLE.toString()))
                    .findFirst()
        }

        fab.setOnClickListener {
            val intent = Intent(this, LightNovelFormActivity::class.java)
            intent.putExtra(JsonKeys.TITLE.toString(), mLightNovel.title)
            intent.putExtra(JsonKeys.AUTHOR.toString(), mLightNovel.author)
            intent.putExtra(JsonKeys.DESCRIPTION.toString(), mLightNovel.description)
            intent.putExtra(JsonKeys.COMPLETED.toString(), mLightNovel.completed)
            intent.putStringArrayListExtra(JsonKeys.TRANSLATOR_SITE.toString(), mLightNovel.translators.map { it.name } as ArrayList<String>)
            intent.putStringArrayListExtra(JsonKeys.GENRES.toString(), mLightNovel.genres.map { it.name } as ArrayList<String>)
            intent.putExtra(REQUEST_CODE_KEY, EDIT_REQUEST_CODE)

            startActivityForResult(intent, LiteraryTrackerUtils.EDIT_REQUEST_CODE)
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> return navigateUpTo(android.content.Intent(this, LightNovelListActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {

        if (requestCode == com.seregil13.literarytracker.util.LiteraryTrackerUtils.EDIT_REQUEST_CODE) {
            realm.executeTransaction {
                mLightNovel = realm.where(com.seregil13.literarytracker.realm.LightNovel::class.java)
                        .equalTo("title", data?.getStringExtra(com.seregil13.literarytracker.util.JsonKeys.TITLE.toString()))
                        .findFirst()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun setData(ln: com.seregil13.literarytracker.realm.LightNovel) {
        this.mLightNovel = ln
    }
}