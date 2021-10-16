package com.example.sample1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sample1.model.commitinfo.Author;
import com.example.sample1.model.commitinfo.CommitInstance;

import java.util.List;

public class CommitInfoAdapter extends RecyclerView.Adapter<CommitInfoAdapter.ViewHolder> {
    private List<CommitInstance> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    CommitInfoAdapter(Context context, List<CommitInstance> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.commit_info_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommitInstance commitInstance = mData.get(position);
        holder.authorNameText.setText(commitInstance.getCommit().getAuthor().getName());
        holder.commitHashText.setText(commitInstance.getSha());
        holder.commitMessageText.setText(commitInstance.getCommit().getMessage());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView authorNameText;
        TextView commitHashText;
        TextView commitMessageText;

        ViewHolder(View itemView) {
            super(itemView);
            authorNameText = itemView.findViewById(R.id.author);
            commitHashText = itemView.findViewById(R.id.commit_hash);
            commitMessageText = itemView.findViewById(R.id.commit_message);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    CommitInstance getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
