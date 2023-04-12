package com.group.comicreader;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group.comicreader.adapters.FavoritesAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private final String TAG = "FavoritesFragment";
    private RecyclerView recyclerFavorites;
    private List<String> favoritesComicID;
    private FirebaseFirestore firestore;
    private FavoritesAdapter adapter;
    private FirebaseAuth auth;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find views
        recyclerFavorites = view.findViewById(R.id.recycler_favorites);

        // Set up Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set up FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Set up adapter and recycler view
        adapter = new FavoritesAdapter(firestore);
        recyclerFavorites.setAdapter(adapter);

        // Find and load favorites
        favoritesComicID = new ArrayList<>();
        firestore.collection("User").document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            favoritesComicID = (List<String>) documentSnapshot.get("favorites");

                            adapter.setFavoritesComicID(favoritesComicID);
                        }
                    }
                });
    }
}