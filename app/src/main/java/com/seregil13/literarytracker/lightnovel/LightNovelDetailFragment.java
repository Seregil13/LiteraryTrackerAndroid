package com.seregil13.literarytracker.lightnovel;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a single LightNovel detail screen.
 * This fragment is either contained in a {@link LightNovelListActivity}
 * in two-pane mode (on tablets) or a {@link LightNovelDetailActivity}
 * on handsets.
 */
public class LightNovelDetailFragment extends Fragment {

    public static final String TAG = "LNDetailFragment";

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_LIGHTNOVEL_ID = "lightnovel_id";
    public static final String ARG_LIGHTNOVEL_TITLE = "lightnovel_title";
    public static final String ARG_LIGHTNOVEL_AUTHOR = "lightnovel_author";

    private static final String JSON_TITLE = "title";
    private static final String JSON_AUTHOR = "author";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_COMPLETED = "completed";
    private static final String JSON_TRANSLATOR_SITE = "translatorSite";
    private static final String JSON_GENRES = "genres";

    private static final ServerInfo.LiteraryType TYPE = ServerInfo.LiteraryType.LIGHT_NOVEL;

    private TextView mTitleTextView;
    private TextView mAuthorTextView;
    private TextView mCompletedTextView;
    private TextView mDescriptionTextView;
    private TextView mTranslatorSiteTextView;

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

        mTitleTextView = (TextView) view.findViewById(R.id.title);
        mAuthorTextView = (TextView) view.findViewById(R.id.author);
        mCompletedTextView = (TextView) view.findViewById(R.id.completionStatus);
        mDescriptionTextView = (TextView) view.findViewById(R.id.description);
        mTranslatorSiteTextView = (TextView) view.findViewById(R.id.translatorSite);

        return view;
    }

    /**
     * The callback function for a successful network query
     */
    Response.Listener<JSONObject> onSuccess = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                mTitleTextView.setText(response.getString(JSON_TITLE));
                mAuthorTextView.setText(response.getString(JSON_AUTHOR));
                mCompletedTextView.setText(response.getString(JSON_COMPLETED));
                mDescriptionTextView.setText(response.getString(JSON_DESCRIPTION));
                mTranslatorSiteTextView.setText(response.getString(JSON_TRANSLATOR_SITE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };

    /**
     * The callback function for a failed network query
     */
    Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("LN LIST ACTIVITY", error.getMessage());
        }
    };
}
