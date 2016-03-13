package com.seregil13.literarytracker.lightnovel;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;
import com.seregil13.literarytracker.views.WrappedLinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A fragment representing a single LightNovel detail screen.
 * This fragment is either contained in a {@link LightNovelListActivity}
 * in two-pane mode (on tablets) or a {@link LightNovelDetailActivity}
 * on handsets.
 */
public class LightNovelDetailFragment extends Fragment {

    public static final String TAG = "LNDetailFragment";

    /* Fragment Arguments */
    public static final String ARG_LIGHTNOVEL_ID = "lightnovel_id";
    public static final String ARG_LIGHTNOVEL_TITLE = "lightnovel_title";
    public static final String ARG_LIGHTNOVEL_AUTHOR = "lightnovel_author";

    /* JSON keys used in the network data */
    private static final String JSON_TITLE = "title";
    private static final String JSON_AUTHOR = "author";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_COMPLETED = "completed";
    private static final String JSON_TRANSLATOR_SITE = "translatorSite";
    private static final String JSON_GENRES = "genres";

    /* Defines the type of literature to be used in the network calls */
    private static final ServerInfo.LiteraryType TYPE = ServerInfo.LiteraryType.LIGHT_NOVEL;

    /* Views */
    private TextView mAuthorTextView;
    private TextView mCompletedTextView;
    private TextView mDescriptionTextView;
    private TextView mTranslatorSiteTextView;
    private WrappedLinearLayout mGenresLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightNovelDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_LIGHTNOVEL_ID)) {

            int id = getArguments().getInt(ARG_LIGHTNOVEL_ID);
            String title = getArguments().getString(ARG_LIGHTNOVEL_TITLE, "Light Novel");

            Log.d(TAG, title);

            /* Sends a request for a json object via volley */
            JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, ServerInfo.getDetailUrl(TYPE, String.valueOf(id)), null, onSuccess, onError);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(json);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lightnovel_detail, container, false);

        mAuthorTextView = (TextView) view.findViewById(R.id.author);
        mCompletedTextView = (TextView) view.findViewById(R.id.completionStatus);
        mDescriptionTextView = (TextView) view.findViewById(R.id.description);
        mTranslatorSiteTextView = (TextView) view.findViewById(R.id.translatorSite);
        mGenresLayout = (WrappedLinearLayout) view.findViewById(R.id.genres);

        return view;
    }

    /**
     * The callback function for a successful network query
     */
    Response.Listener<JSONObject> onSuccess = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                mAuthorTextView.setText(response.getString(JSON_AUTHOR));
                mCompletedTextView.setText(Boolean.parseBoolean(response.getString(JSON_COMPLETED)) ? "Completed" : "In Progress");
                mDescriptionTextView.setText(String.format("%s", response.getString(JSON_DESCRIPTION)));
                mTranslatorSiteTextView.setText(String.format("%s", response.getString(JSON_TRANSLATOR_SITE)));

                ArrayList<String> genres = LiteraryTrackerUtils.jsonArrayToList(response.getJSONArray(JSON_GENRES));
                for (String genre : genres) {

                    /* Creates a textview to hold the genre */
                    TextView genreTV = new TextView(getContext());
                    genreTV.setText(genre);
                    genreTV.setBackgroundResource(R.drawable.border);
                    genreTV.setTextColor(Color.WHITE);
                    genreTV.setPadding(15,10,15,10);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,10,10,10);
                    genreTV.setLayoutParams(params);

                    mGenresLayout.addView(genreTV);
                }

                ((CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout)).setTitle(response.getString(JSON_TITLE));
            } catch (Exception e) {
                e.printStackTrace(); // TODO: Handle exceptions better
            }
        }
    };

    /**
     * The callback function for a failed network query
     */
    Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, error.getMessage());
        }
    };
}
