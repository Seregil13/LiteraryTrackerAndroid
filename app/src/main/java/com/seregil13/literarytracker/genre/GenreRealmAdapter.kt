package com.seregil13.literarytracker.genre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.seregil13.literarytracker.realm.Genre
import io.realm.RealmBaseAdapter
import io.realm.RealmResults
import kotlin.properties.Delegates

open class GenreRealmAdapter(realmResults: RealmResults<Genre>):
        RealmBaseAdapter<Genre>(realmResults), ListAdapter {

    private class ViewHolder {
        internal var genre: TextView by Delegates.notNull()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)
            viewHolder = ViewHolder()
            viewHolder.genre = view!!.findViewById(android.R.id.text1) as TextView
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        if (adapterData != null) {
            val item = adapterData!![position]
            viewHolder.genre.text = item.name
        }
        return view
    }

}