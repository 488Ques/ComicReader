package com.group.comicreader.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group.comicreader.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<String> favorites;

    public FavoritesAdapter(List<String> favorites) {
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_comic, parent, false);
        return new ViewHolder(view);
    }

    // TODO: Finish implementing this
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String comicID = favorites.get(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageFavoriteCover;
        public TextView textFavoriteTitle;
        public TextView textFavoriteAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFavoriteCover = itemView.findViewById(R.id.image_favorite_cover);
            textFavoriteTitle = itemView.findViewById(R.id.text_favorite_title);
            textFavoriteAuthor = itemView.findViewById(R.id.text_favorite_author);
        }

        public void Bind(String imageUrl, String title, String author) {
            Picasso.get().load(imageUrl).into(imageFavoriteCover);
            textFavoriteTitle.setText(title);
            textFavoriteAuthor.setText(author);
        }
    }
}
