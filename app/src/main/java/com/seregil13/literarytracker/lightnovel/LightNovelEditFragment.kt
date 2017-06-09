package com.seregil13.literarytracker.lightnovel

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.seregil13.literarytracker.R
import com.seregil13.literarytracker.translator.TranslatorSelectionActivity
import com.seregil13.literarytracker.genre.GenreSelectionActivity
import com.seregil13.literarytracker.realm.Genre
import com.seregil13.literarytracker.realm.LightNovel
import com.seregil13.literarytracker.realm.Translator
import com.seregil13.literarytracker.util.*
import io.realm.Realm
import io.realm.exceptions.RealmException
import kotlinx.android.synthetic.main.fragment_light_novel_edit.*
import kotlin.properties.Delegates

class LightNovelEditFragment: Fragment() {

    enum class Mode {
        CREATE,
        EDIT
    }

    private var mTitle: String = ""
    private var mAuthor: String = ""
    private var mDescription: String = ""
    private var mCompleted: Boolean = false
    private var mTranslator: ArrayList<Translator> = ArrayList()
    private var mGenres: ArrayList<Genre> = ArrayList()

    private var mCreateOrEdit: Mode = Mode.CREATE

    private var realm: Realm by Delegates.notNull()

    companion object {
        /**
         * Creates a new instance of the fragment in edit mode with all the pertinent data passed in as
         * parameters.

         * @return An instance of LightNovelEditFragment.
         */
        fun newEditInstance(bundle: Bundle): LightNovelEditFragment {
            val fragment = LightNovelEditFragment()
            bundle.putSerializable(CREATE_OR_EDIT_KEY, Mode.EDIT)

            fragment.arguments = bundle
            return fragment
        }

        /**
         * Creates a new instance of the fragment in create mode with all the pertinent data passed in as
         * parameters.

         * @return An instance of LightNovelEditFragment.
         */
        fun newCreateInstance(): LightNovelEditFragment {
            val fragment = LightNovelEditFragment()
            val arguments = Bundle()
            arguments.putSerializable(CREATE_OR_EDIT_KEY, Mode.CREATE)

            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()

        val arguments = arguments
        this.mCreateOrEdit = arguments.getSerializable(CREATE_OR_EDIT_KEY) as LightNovelEditFragment.Mode

        when (this.mCreateOrEdit) {
            LightNovelEditFragment.Mode.CREATE -> {
                this.mTitle = ""
                this.mAuthor = ""
                this.mDescription = ""
                this.mCompleted = false
                this.mTranslator = ArrayList<Translator>()
                this.mGenres = ArrayList<Genre>()
            }
            LightNovelEditFragment.Mode.EDIT -> {
                this.mTitle = arguments.getString(JsonKeys.TITLE.toString()) // TODO: move jsonkeys to util file
                this.mAuthor = arguments.getString(JsonKeys.AUTHOR.toString())
                this.mDescription = arguments.getString(JsonKeys.DESCRIPTION.toString())
                this.mCompleted = arguments.getBoolean(JsonKeys.COMPLETED.toString())

                val translatorSiteKeys: ArrayList<String> = arguments.getStringArrayList(JsonKeys.TRANSLATOR_SITE.toString())
                val translatorSiteQuery = realm.where(Translator::class.java)
                translatorSiteKeys.forEach { key -> translatorSiteQuery.or().equalTo("name", key) }
                val translatorSiteResult = translatorSiteQuery.findAll()
                mTranslator.addAll(translatorSiteResult.map { it } as Collection<Translator>)

                val genreKeys: java.util.ArrayList<String> = arguments.getStringArrayList(JsonKeys.GENRES.toString())
                val genreQuery = realm.where(Genre::class.java)
                genreKeys.forEach { key -> genreQuery.or().equalTo("name", key) }
                val genreResult = genreQuery.findAll()
                mGenres.addAll(genreResult.map { it } as Collection<Genre>)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_light_novel_edit, container, false)

        view.findViewById(R.id.cancelButton).setOnClickListener {
            activity.setResult(EDIT_CANCEL_CODE)
            activity.finish()
        }
        view.findViewById(R.id.saveButton).setOnClickListener {
            try {
                val ln: LightNovel = LightNovel()
                ln.title = title.text.toString()
                ln.author = author.text.toString()
                ln.description = description.text.toString()
                ln.translators.addAll(mTranslator)
                ln.genres.addAll(mGenres)

                realm.executeTransaction {
                    realm.copyToRealmOrUpdate(ln)
                }

                // TODO: validation
            } catch (e: RealmException) {
                e.printStackTrace()
            }
        }
        view.findViewById(R.id.genreSelection).setOnClickListener {
            val intent = Intent(activity.applicationContext, GenreSelectionActivity::class.java)
            intent.putStringArrayListExtra(JsonKeys.GENRES.toString(), mGenres.map { it.name } as ArrayList<String>)

            startActivityForResult(intent, GENRE_REQUEST_CODE)
        }

        (view.findViewById(R.id.title) as TextView).text = mTitle
        (view.findViewById(R.id.author) as TextView).text = mAuthor
        (view.findViewById(R.id.description) as TextView).text = mDescription
        view.findViewById(R.id.translatorSite).setOnClickListener {
            val intent = Intent(activity, TranslatorSelectionActivity::class.java)
            intent.putStringArrayListExtra(JsonKeys.TRANSLATOR_SITE.toString(), mTranslator.map { it.name } as ArrayList<String>)

            startActivityForResult(intent, TRANSLATOR_SITE_REQUEST_CODE)
        }
        (view.findViewById(R.id.completionStatus) as CheckBox).isChecked = mCompleted

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GENRE_REQUEST_CODE -> {
                if (resultCode == GENRE_SUCCESS_CODE) {
                    /* Updates the stored genres to be the selection returned from the Genre Selection Activity */

                    val genreKeys: ArrayList<String>? = data?.getStringArrayListExtra(JsonKeys.GENRES.toString())
                    val genreQuery = realm.where(Genre::class.java)
                    genreKeys?.forEach { key -> genreQuery.or().equalTo("name", key) }
                    val genreResult = genreQuery.findAll()
                    mGenres.addAll(genreResult.map { it } as Collection<Genre>)
                }
            }
            TRANSLATOR_SITE_REQUEST_CODE -> {
                if (resultCode == TRANSLATOR_SITE_SUCCESS_CODE) {
                    val keys: ArrayList<String>? = data?.getStringArrayListExtra(JsonKeys.TRANSLATOR_SITE.toString())
                    val query = realm.where(Translator::class.java)
                    keys?.forEach { key -> query.or().equalTo("name", key) }
                    val result = query.findAll()
                    mTranslator.addAll(result.map { it } as Collection<Translator>)
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}