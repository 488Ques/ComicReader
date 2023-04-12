package com.group.comicreader.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group.comicreader.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<String> favoritesComicID;
    private FirebaseFirestore firestore;

    public FavoritesAdapter(List<String> favoritesComicID, FirebaseFirestore firestore) {
        this.favoritesComicID = favoritesComicID;
        this.firestore = firestore;
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
        String comicID = favoritesComicID.get(position);

        firestore.collection("Comic").document(comicID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        String title = documentSnapshot.getString("title");
                        String author = documentSnapshot.getString("author");

                        holder.Bind(imageUrl, title, author);
                    }
                });
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
