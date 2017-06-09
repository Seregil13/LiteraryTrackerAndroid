package com.seregil13.literarytracker.translator

import android.app.AlertDialog
import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.EditText
import android.widget.ListView
import com.seregil13.literarytracker.R
import com.seregil13.literarytracker.util.FAILURE_CODE
import com.seregil13.literarytracker.util.SUCCESS_CODE
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_selection.*
import kotlin.properties.Delegates

/**
 * Base class for a multi-select list of simple items displaying only strings
 *
 * TODO: Look into using recycler view instead of ListActivity
 */
abstract class SelectionActivity: ListActivity() {

    protected var realm: Realm by Delegates.notNull()
    protected val items: MutableList<String> = mutableListOf()
    abstract var ITEMS_KEY: String
    abstract var createItemMessage: String
    abstract var createItemTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        realm = Realm.getDefaultInstance()

        listView.isTextFilterEnabled = true
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        if (savedInstanceState == null) {
            items.addAll(intent.getStringArrayListExtra(ITEMS_KEY))

            initFab()
            initButtons()
            initAdapter()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun initFab() {
        fab.setOnClickListener {
            val editText = EditText(this)
            AlertDialog.Builder(this)
                    .setMessage(createItemMessage)
                    .setTitle(createItemTitle)
                    .setView(editText)
                    .setPositiveButton(R.string.confirm, { _, _ -> positiveButton(editText) })
                    .setNegativeButton(R.string.cancel, { dialog, _ -> dialog.dismiss() })
                    .show()
        }
    }

    /**
     * Initializes the save and cancel buttons
     */
    fun initButtons() {
        saveButton.setOnClickListener {
            val arr: SparseBooleanArray = listView.checkedItemPositions

            val selectedItems = (0..arr.size() -1)
                    .map { arr.keyAt(it) }
                    .filter { arr.get(it) }
                    .mapTo(ArrayList<String>()) { listView.getItemAtPosition(it) as String }

            val returnData = Intent()
            returnData.putStringArrayListExtra(ITEMS_KEY, selectedItems)
            setResult(SUCCESS_CODE)
            finish()
        }
        cancelButton.setOnClickListener {
            setResult(FAILURE_CODE)
            finish()
        }
    }

    /**
     * Initializes the adapter and sets the initial content.
     *
     * Remember to set the listviews adapter
     */
    abstract fun initAdapter()

    /**
     * The action to take on the positive button of the dialog shown when creating a new item
     */
    abstract fun positiveButton(editText: EditText)
}