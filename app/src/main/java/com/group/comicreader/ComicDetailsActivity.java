package com.group.comicreader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group.comicreader.adapters.ChapterListAdapter;
import com.group.comicreader.models.Chapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ComicDetailsActivity extends AppCompatActivity {
    final String TAG = "ComicDetailsActivity";
    final int MAX_LINES = 3;

    private RecyclerView chaptersRecyclerView;
    private ChapterListAdapter chapterListAdapter;
    private FirebaseFirestore firestore;

//    TextView titleTextView;
//    TextView authorTextView;
//    TextView genresTextView;
    TextView descriptionTextView;
    Button showMoreButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_details);

        // Get comicID
        Intent intent = getIntent();
        String comicID = intent.getStringExtra("comicID");
        Log.d(TAG, "Comic ID is " + comicID);

        // Set up Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_details);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        // Show back button and handle its click event
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Find views
        TextView titleTextView = findViewById(R.id.text_details_title);
        TextView authorTextView = findViewById(R.id.text_details_author);
        TextView genresTextView = findViewById(R.id.text_details_genres);
        descriptionTextView = findViewById(R.id.text_details_description);

        showMoreButton = findViewById(R.id.button_details_show_more);
        ImageView coverImageView = findViewById(R.id.image_details_cover);
        chaptersRecyclerView = findViewById(R.id.recycler_details_chapters);

        // Set comic details
        DocumentReference docRef = firestore.collection("Comic").document(comicID);

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String title = documentSnapshot.getString("title");
                        String author = documentSnapshot.getString("author");
                        String description = documentSnapshot.getString("description");
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        List<String> genres = (List<String>) documentSnapshot.get("genres");

                        titleTextView.setText(title);
                        authorTextView.setText(author);
                        genresTextView.setText(TextUtils.join(", ", genres));
                        descriptionTextView.setText(description);
                        Picasso.get().load(imageUrl).into(coverImageView);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        showMoreIfOk();
                    }
                });

        // Add sample chapters
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(new Chapter(1, "Chapter 1 Title", "2022-04-01"));
        chapters.add(new Chapter(2, "Chapter 2 Title", "2022-04-15"));
        chapters.add(new Chapter(3, "Chapter 3 Title", "2022-05-01"));


        // Set up chapters recycler view
        chapterListAdapter = new ChapterListAdapter(chapters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chaptersRecyclerView.setLayoutManager(layoutManager);
        chaptersRecyclerView.setAdapter(chapterListAdapter);
    }

    // Show or hide Show more button depending on whether description text is longer than 3 lines
    private void showMoreIfOk() {
        ViewTreeObserver observer = descriptionTextView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (descriptionTextView.getLineCount() > MAX_LINES) {
                    showMoreButton.setVisibility(View.VISIBLE);

                    descriptionTextView.setMaxLines(MAX_LINES);
                    descriptionTextView.setEllipsize(TextUtils.TruncateAt.END);

                    // Handle click event of show more button
                    showMoreButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (descriptionTextView.getMaxLines() == Integer.MAX_VALUE) {
                                // The text is expanded, so collapse it and change the button text to "Show more"
                                descriptionTextView.setMaxLines(3);
                                descriptionTextView.setEllipsize(TextUtils.TruncateAt.END);
                                showMoreButton.setText("Show more");
                            } else {
                                // The text is collapsed, so expand it and change the button text to "Show less"
                                descriptionTextView.setMaxLines(Integer.MAX_VALUE);
                                descriptionTextView.setEllipsize(null);
                                showMoreButton.setText("Show less");
                            }
                        }
                    });

                } else {
                    showMoreButton.setVisibility(View.GONE);
                }
                descriptionTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comic_details, menu);
        return super.onCreateOptionsMenu(menu);
    }
}