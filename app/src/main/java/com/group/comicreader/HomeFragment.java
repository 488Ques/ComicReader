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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group.comicreader.adapters.ComicListAdapter;
import com.group.comicreader.models.ComicListItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private final String TAG = "HomeFragment";
    private RecyclerView recyclerComicList;
    private List<ComicListItem> comicList;
    private FirebaseFirestore firestore;
    private ComicListAdapter comicListAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find views
        recyclerComicList = view.findViewById(R.id.recycler_comic_list);

        // Set up Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set up adapter and recycler
        comicListAdapter = new ComicListAdapter();
        recyclerComicList.setAdapter(comicListAdapter);

        // Find and load comics into recycler view
        loadComics();
    }

    private void loadComics() {
        comicList = new ArrayList<>();
        firestore.collection("Comic")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Populate comicList
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String id = documentSnapshot.getId();
                            String title = documentSnapshot.getString("title");
                            String imageUrl = documentSnapshot.getString("imageUrl");
                            comicList.add(new ComicListItem(id, title, imageUrl));
                        }

                        for (ComicListItem item : comicList) {
                            Log.d(TAG, "onSuccess: comic item: " + item);
                        }

                        comicListAdapter.setComicList(comicList);
                    }
                });
    }
}