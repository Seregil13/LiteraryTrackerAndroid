package com.seregil13.literarytracker.lightnovel

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.seregil13.literarytracker.realm.LightNovel

open class LightNovelViewHolder(val view: android.view.View): android.support.v7.widget.RecyclerView.ViewHolder(view) {
    val mTitleView = view.findViewById(com.seregil13.literarytracker.R.id.title) as android.widget.TextView
    val mAuthorView = view.findViewById(com.seregil13.literarytracker.R.id.author) as android.widget.TextView
    val mView = view

    var mItem: com.seregil13.literarytracker.realm.LightNovel = com.seregil13.literarytracker.realm.LightNovel()
}