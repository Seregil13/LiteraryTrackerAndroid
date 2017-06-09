package com.seregil13.literarytracker.lightnovel

open class LightNovelAdapter(val items: MutableList<com.seregil13.literarytracker.realm.LightNovel>, val onClickCallback: com.seregil13.literarytracker.lightnovel.LightNovelAdapter.OnClickCallback): android.support.v7.widget.RecyclerView.Adapter<LightNovelViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): LightNovelViewHolder {
        val view = android.view.LayoutInflater.from(parent.context).inflate(com.seregil13.literarytracker.R.layout.lightnovel_list_content, parent, false)
        return LightNovelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LightNovelViewHolder, position: Int) {
        holder.mItem = items[position]
        holder.mTitleView.text = items[position].title
        holder.mAuthorView.text = items[position].author

        holder.mView.setOnClickListener { onClickCallback.OnCLick(holder.mItem) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open fun updateNovelList(novels: List<com.seregil13.literarytracker.realm.LightNovel>) {
        items.clear()
        items.addAll(novels)
        notifyDataSetChanged()
    }

    interface OnClickCallback {
        fun OnCLick(item: com.seregil13.literarytracker.realm.LightNovel)
    }

}

