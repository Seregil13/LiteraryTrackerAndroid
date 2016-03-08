package com.seregil13.literarytracker;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LightNovelDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LightNovelDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LightNovelDetailFragment extends Fragment {

    // TODO: Make a master detail activity for tablets

    private static final String ARG_LIGHT_NOVEL_ID = "lightnovel_id";
    private static final String ARG_LIGHT_NOVEL_TITLE = "lightnovel_title";
    private static final String ARG_LIGHT_NOVEL_AUTHOR = "lightnovel_author";

    private static final String JSON_TITLE = "title";
    private static final String JSON_AUTHOR = "author";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_COMPLETED = "completed";
    private static final String JSON_TRANSLATOR_SITE = "translatorSite";
    private static final String JSON_GENRES = "genres";

    private TextView mTitleTextView;
    private TextView mAuthorTextView;
    private TextView mCompletedTextView;
    private TextView mDescriptionTextView;
    private TextView mTranslatorSiteTextView;

    private OnFragmentInteractionListener mListener;

    public LightNovelDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment LightNovelDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LightNovelDetailFragment newInstance(int id, String title, String author) {
        LightNovelDetailFragment fragment = new LightNovelDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIGHT_NOVEL_ID, id);
        args.putString(ARG_LIGHT_NOVEL_TITLE, title);
        args.putString(ARG_LIGHT_NOVEL_AUTHOR, author);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            new FetchLightNovelDetails().execute(getArguments().getInt(ARG_LIGHT_NOVEL_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_light_novel_detail,container, false);

        mTitleTextView = (TextView) view.findViewById(R.id.title);
        mAuthorTextView = (TextView) view.findViewById(R.id.author);
        mCompletedTextView = (TextView) view.findViewById(R.id.completionStatus);
        mDescriptionTextView = (TextView) view.findViewById(R.id.description);
        mTranslatorSiteTextView = (TextView) view.findViewById(R.id.translatorSite);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class FetchLightNovelDetails extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {

            String result = "";

            try {

                URL url = new URL("http://seregil13.com/lightnovels/getLightNovel/" + params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    result = readStream(in);
                } finally {
                    connection.disconnect();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(String s) {
            Log.d("JSON", s);

            try {
                JSONObject json = new JSONObject(s);

                mTitleTextView.setText(json.getString(JSON_TITLE));
                mAuthorTextView.setText(json.getString(JSON_AUTHOR));
                mDescriptionTextView.setText(json.getString(JSON_DESCRIPTION));
                mCompletedTextView.setText(json.getBoolean(JSON_COMPLETED) ? "completed" : "in progress");
                mTranslatorSiteTextView.setText(json.getString(JSON_TRANSLATOR_SITE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String readStream(InputStream inputStream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder res = new StringBuilder();

            String line = "";
            while((line = reader.readLine()) != null) {
                res.append(line);
            }

            return res.toString();
        }
    }
}
