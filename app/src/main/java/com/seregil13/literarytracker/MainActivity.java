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

package com.seregil13.literarytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.seregil13.literarytracker.lightnovel.LightNovelListActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<String> s = new ArrayList<>(3);
        s.add("Light Novels");
        s.add("Books");
        s.add("Manga");

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setAdapter(new SimpleRecyclerAdapter(s));
    }

    class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.SimpleViewHolder> {

        List<String> items;

        SimpleRecyclerAdapter(List<String> items) {
            this.items = items;
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleViewHolder holder, int position) {
            holder.name.setText(items.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (holder.getAdapterPosition()) {
                        case 0:
                            Intent intent = new Intent(MainActivity.this, LightNovelListActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Toast.makeText(view.getContext(), "TODO", Toast.LENGTH_SHORT).show();
                        case 2:
                            Toast.makeText(view.getContext(), "TODO", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class SimpleViewHolder extends RecyclerView.ViewHolder {

            TextView name;

            SimpleViewHolder(View itemView) {
                super(itemView);
                this.name = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
