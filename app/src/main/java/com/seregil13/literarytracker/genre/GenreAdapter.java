package com.seregil13.literarytracker.genre;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerDbHelper;
import com.seregil13.literarytracker.sqlite.util.GenreDb;

import java.util.List;

/**
 * @author seregil13
 */
public class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<GenreContent> items;

    public GenreAdapter(List<GenreContent> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).name.setText(items.get(position).name);
            holder.itemView.setBackgroundColor(items.get(position).isSelected ? Color.GRAY : Color.WHITE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    items.get(holder.getAdapterPosition()).isSelected = !items.get(holder.getAdapterPosition()).isSelected;
                    holder.itemView.setBackgroundColor(items.get(holder.getAdapterPosition()).isSelected ? Color.GRAY : Color.WHITE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateGenreList(List<GenreContent> items) {
        this.items.clear();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public List<GenreContent> getItems() {
        return this.items;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView name;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            this.name = (TextView) view.findViewById(R.id.genre_name);
        }
    }
}
