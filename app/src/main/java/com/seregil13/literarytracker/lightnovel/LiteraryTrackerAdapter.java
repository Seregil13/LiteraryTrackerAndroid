package com.seregil13.literarytracker.lightnovel;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.genre.GenreContent;
import com.seregil13.literarytracker.util.JsonKeys;

import java.util.List;

/**
 * @author Alec
 * @since Feb 22, 2017
 */
class LiteraryTrackerAdapter extends RecyclerView.Adapter<LiteraryTrackerAdapter.ViewHolder> {

    private List<ListContent> contents;

    LiteraryTrackerAdapter(List<ListContent> items) {
        contents = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lightnovel_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = contents.get(position);
        holder.mTitleView.setText(contents.get(position).title);
        holder.mAuthorView.setText(contents.get(position).author);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, LightNovelDetailActivity.class);
                intent.putExtra(JsonKeys.ID.toString(), holder.mItem.id);
                intent.putExtra(JsonKeys.TITLE.toString(), holder.mItem.title);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    void updateNovelList(List<ListContent> novels) {
        this.contents.clear();
        this.contents.addAll(novels);
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitleView;
        final TextView mAuthorView;
        ListContent mItem;

        ViewHolder(View view) {
            super(view);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mAuthorView = (TextView) view.findViewById(R.id.author_label);
        }
    }
}
