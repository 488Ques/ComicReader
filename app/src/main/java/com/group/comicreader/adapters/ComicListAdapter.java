package com.group.comicreader.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group.comicreader.ComicDetailsActivity;
import com.group.comicreader.R;
import com.group.comicreader.models.Comic;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComicListAdapter extends RecyclerView.Adapter<ComicListAdapter.ViewHolder> {
    private List<Comic> comicList;

    public ComicListAdapter(List<Comic> comicList) {
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comic, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comic comic = comicList.get(position);
        holder.comicTitle.setText(comic.getTitle());
        Picasso.get().load(comic.getCover()).into(holder.comicCover);
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView comicCover;
        public TextView comicTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comicCover = itemView.findViewById(R.id.image_comic_cover);
            comicTitle = itemView.findViewById(R.id.text_comic_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ComicDetailsActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}
