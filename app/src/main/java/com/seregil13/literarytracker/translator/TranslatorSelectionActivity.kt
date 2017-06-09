package com.seregil13.literarytracker.translator

import android.os.Bundle
import android.widget.EditText
import com.seregil13.literarytracker.R
import com.seregil13.literarytracker.genre.TranslatorRealmAdapter
import com.seregil13.literarytracker.realm.Translator
import com.seregil13.literarytracker.util.KEY_TRANSLATOR
import io.realm.RealmResults

// TODO don't use list activity
class TranslatorSelectionActivity : SelectionActivity() {

    override var ITEMS_KEY = KEY_TRANSLATOR
    override var createItemMessage: String = getString(R.string.new_translator_message)
    override var createItemTitle  = getString(R.string.new_translator_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initAdapter() {
        val translators: RealmResults<Translator> = realm.where(Translator::class.java).findAll()
        val adapter = TranslatorRealmAdapter(translators)
        listView.adapter = adapter

        translators.withIndex().forEach { (index, translator: Translator) ->
            if (items.contains(translator.name)) listView.setItemChecked(index, true)
        }
    }

    override fun positiveButton(editText: EditText) {
        realm.executeTransaction {
            val translator = Translator()
            translator.name = editText.text.toString()

            realm.copyToRealmOrUpdate(translator)
        }
    }
}