package com.seregil13.literarytracker.genre

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.seregil13.literarytracker.realm.Translator
import io.realm.RealmBaseAdapter
import io.realm.RealmResults
import kotlin.properties.Delegates

/**
 * Created by Alec on 6/8/2017.
 */
class TranslatorRealmAdapter(realmResults: RealmResults<Translator>):
        RealmBaseAdapter<Translator>(realmResults) {


    private class ViewHolder {
        internal var translator: TextView by Delegates.notNull()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)
            viewHolder = ViewHolder()
            viewHolder.translator = view!!.findViewById(android.R.id.text1) as TextView
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        if (adapterData != null) {
            val item = adapterData!![position]
            viewHolder.translator.text = item.name
        }
        return view
    }
}