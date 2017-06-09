package com.seregil13.literarytracker.lightnovel

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.seregil13.literarytracker.R
import com.seregil13.literarytracker.realm.Genre
import com.seregil13.literarytracker.realm.LightNovel
import com.seregil13.literarytracker.util.JsonKeys
import com.seregil13.literarytracker.util.LiteraryTrackerUtils
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_lightnovel_list.*
import kotlinx.android.synthetic.main.lightnovel_list.*
import kotlin.properties.Delegates

class LightNovelListActivity: AppCompatActivity(), LightNovelAdapter.OnClickCallback {

    companion object {
        val TAG: String = LightNovelListActivity::class.java.simpleName
    }

    private var realm: Realm by Delegates.notNull()
    private var mAdapter: LightNovelAdapter by Delegates.notNull()

    private var mTwoPane: Boolean by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lightnovel_list)
        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener {
            val intent: Intent = Intent(this, LightNovelFormActivity::class.java)
            startActivityForResult(intent, LiteraryTrackerUtils.CREATE_REQUEST_CODE)
        }

        realm = Realm.getDefaultInstance()

        val ln: MutableList<LightNovel> = mutableListOf()

        realm.executeTransaction {
            ln.addAll(realm.where(LightNovel::class.java).findAll())
//            realm.createObject(Genre::class.java, "action")
        }
        mAdapter = LightNovelAdapter(ln, this)
        lightnovel_list.layoutManager = LinearLayoutManager(this)
        lightnovel_list.adapter = mAdapter

        mTwoPane = lightnovel_detail_container != null
    }

    // TODO: handle empty state

    override fun OnCLick(item: LightNovel) {
        if (mTwoPane) {
            val arguments = Bundle()
            arguments.putString(JsonKeys.TITLE.toString(), item.title)
            val fragment = LightNovelDetailsFragment()
            fragment.arguments = arguments
            supportFragmentManager.beginTransaction()
                    .replace(R.id.lightnovel_detail_container, fragment)
                    .commit()
        } else {
            val intent = Intent(this, LightNovelDetailActivity::class.java)
            intent.putExtra(JsonKeys.TITLE.toString(), item.title)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LiteraryTrackerUtils.CREATE_REQUEST_CODE) {
            realm.executeTransaction { mAdapter.updateNovelList(realm.where(LightNovel::class.java).findAll()) }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}