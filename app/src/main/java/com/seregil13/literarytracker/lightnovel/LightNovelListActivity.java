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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.network.FetchListTask;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.TaskListener;

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
public class LightNovelListActivity extends AppCompatActivity implements TaskListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_AUTHOR = "author";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightnovel_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        new FetchListTask(this, ServerInfo.LiteraryType.LIGHT_NOVEL).execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
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

    @Override
    public void onTaskCompleted(String result) {
        try {
            JSONArray novels = new JSONArray(result);
            List<LightNovelListContent.LightNovel> list = new ArrayList<>();

            for (int i = 0; i < novels.length(); ++i) {
                JSONObject novel = novels.getJSONObject(i);
                list.add(new LightNovelListContent.LightNovel(novel.getInt(JSON_ID), novel.getString(JSON_TITLE), novel.getString(JSON_AUTHOR)));
            }

            this.adapter.updateNovelList(list);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
                        arguments.putString(LightNovelDetailFragment.ARG_LIGHTNOVEL_ID, String.valueOf(holder.mItem.id));
                        LightNovelDetailFragment fragment = new LightNovelDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.lightnovel_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, LightNovelDetailActivity.class);
                        intent.putExtra(LightNovelDetailFragment.ARG_LIGHTNOVEL_ID, holder.mItem.id);

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
