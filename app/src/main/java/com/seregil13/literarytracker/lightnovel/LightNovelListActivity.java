/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Alec Rietman
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.seregil13.literarytracker.lightnovel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;
import com.seregil13.literarytracker.util.JsonKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of LightNovels. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LightNovelDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class LightNovelListActivity extends AppCompatActivity {

    private static final String TAG = "LN_LISTACTIVITY";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightnovel_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        /* Use the volley library to send a request */
        JsonArrayRequest json = new JsonArrayRequest(Request.Method.GET, ServerInfo.getListUrl(ServerInfo.LiteraryType.LIGHT_NOVEL), null, onSuccess, onError);
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(json);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG) // TODO Make it go to new ln page
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.lightnovel_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.lightnovel_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(new ArrayList<LightNovelListContent.LightNovel>());

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }

    Response.Listener<JSONArray> onSuccess = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            List<LightNovelListContent.LightNovel> list = new ArrayList<>();

            try {
                for (int i = 0; i < response.length(); ++i) {
                    JSONObject novel = response.getJSONObject(i);
                    int id = novel.getInt(JsonKeys.LightNovel.ID);
                    String title  = novel.getString(JsonKeys.LightNovel.TITLE);
                    String author = novel.getString(JsonKeys.LightNovel.AUTHOR);

                    list.add(new LightNovelListContent.LightNovel(id, title, author));
                }
            } catch (JSONException e) {
                e.printStackTrace(); //TODO:
            }

            adapter.updateNovelList(list);
        }

    };

    Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, error.getMessage());
        }
    };


    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<LightNovelListContent.LightNovel> mNovels;

        public SimpleItemRecyclerViewAdapter(List<LightNovelListContent.LightNovel> items) {
            mNovels = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lightnovel_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mNovels.get(position);
            holder.mTitleView.setText(mNovels.get(position).title);
            holder.mAuthorView.setText(mNovels.get(position).author);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(LightNovelDetailFragment.ARG_LIGHTNOVEL_ID, holder.mItem.id);
                        arguments.putString(LightNovelDetailFragment.ARG_LIGHTNOVEL_TITLE, holder.mItem.title);
                        LightNovelDetailFragment fragment = new LightNovelDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.lightnovel_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, LightNovelDetailActivity.class);
                        intent.putExtra(LightNovelDetailFragment.ARG_LIGHTNOVEL_ID, holder.mItem.id);
                        intent.putExtra(LightNovelDetailFragment.ARG_LIGHTNOVEL_TITLE, holder.mItem.title);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mNovels.size();
        }

        public void updateNovelList(List<LightNovelListContent.LightNovel> novels) {
            this.mNovels.clear();
            this.mNovels.addAll(novels);
            this.notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mAuthorView;
            public LightNovelListContent.LightNovel mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.title);
                mAuthorView = (TextView) view.findViewById(R.id.author);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mAuthorView.getText() + "'";
            }
        }
    }
}
