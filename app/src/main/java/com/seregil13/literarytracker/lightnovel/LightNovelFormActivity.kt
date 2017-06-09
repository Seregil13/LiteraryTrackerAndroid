package com.seregil13.literarytracker.lightnovel

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.seregil13.literarytracker.R
import com.seregil13.literarytracker.util.*
import kotlinx.android.synthetic.main.activity_lightnovel_list.*

class LightNovelFormActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_novel_form)

        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val requestCode = intent.getIntExtra(REQUEST_CODE_KEY, CREATE_REQUEST_CODE)

            val fragment: Fragment

            when (requestCode) {
                EDIT_REQUEST_CODE -> fragment = LightNovelEditFragment.newEditInstance(intent.extras)
                CREATE_REQUEST_CODE -> fragment = LightNovelEditFragment.newCreateInstance()
                else -> fragment = LightNovelEditFragment.newCreateInstance()
            }

            supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        intent?.putExtra(REQUEST_CODE_KEY, requestCode)
        super.startActivityForResult(intent, requestCode)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}