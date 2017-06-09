package com.seregil13.literarytracker.genre

import android.os.Bundle
import android.widget.EditText
import com.seregil13.literarytracker.R
import com.seregil13.literarytracker.realm.Genre
import com.seregil13.literarytracker.translator.SelectionActivity
import com.seregil13.literarytracker.util.KEY_GENRES
import io.realm.RealmResults

// TODO don't use list activity
class GenreSelectionActivity: SelectionActivity() {

    override var ITEMS_KEY = KEY_GENRES
    override var createItemMessage = getString(R.string.new_genre_message)
    override var createItemTitle = getString(R.string.new_genre_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


    override fun initAdapter() {
        val genres: RealmResults<Genre> = realm.where(Genre::class.java).findAll()
        val adapter = GenreRealmAdapter(genres)
        listView.adapter = adapter

        genres.withIndex().forEach { (index, genre: Genre) ->
            if (items.contains(genre.name)) listView.setItemChecked(index, true)
        }
    }

    override fun positiveButton(editText: EditText) {
        realm.executeTransaction {
            val genre = Genre()
            genre.name = editText.text.toString()

            realm.copyToRealmOrUpdate(genre)
        }
    }
}
