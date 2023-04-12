package com.group.comicreader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.group.comicreader.adapters.ChapterListAdapter;
import com.group.comicreader.models.Chapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComicDetailsActivity extends AppCompatActivity {
    final String TAG = "ComicDetailsActivity";
    final int MAX_LINES = 3;

    private ChapterListAdapter chapterListAdapter;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView genresTextView;
    private TextView descriptionTextView;
    private Button showMoreButton;
    private ImageView coverImageView;
    private RecyclerView chaptersRecyclerView;
    private String comicID;
    private boolean isComicFavorited;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_details);

        // Get comicID
        Intent intent = getIntent();
        comicID = intent.getStringExtra("comicID");
        Log.d(TAG, "Comic ID is " + comicID);

        // Set up Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set up FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_details);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        // Show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Handle click event of back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Find views
        titleTextView = findViewById(R.id.text_details_title);
        authorTextView = findViewById(R.id.text_details_author);
        genresTextView = findViewById(R.id.text_details_genres);
        descriptionTextView = findViewById(R.id.text_details_description);

        showMoreButton = findViewById(R.id.button_details_show_more);
        coverImageView = findViewById(R.id.image_details_cover);
        chaptersRecyclerView = findViewById(R.id.recycler_details_chapters);

        // Query for comic details
        firestore.collection("Comic")
                .document(comicID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String title = documentSnapshot.getString("title");
                        String author = documentSnapshot.getString("author");
                        String description = documentSnapshot.getString("description");
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        List<String> genres = (List<String>) documentSnapshot.get("genres");

                        // Set comic details
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

        // Query for chapters
        firestore.collection("/Comic/" + comicID + "/Chapter")
                .orderBy("chapterNumber")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    List<Chapter> chapters = new ArrayList<>();

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String id = documentSnapshot.getId();
                            int chapterNumber = documentSnapshot.getLong("chapterNumber").intValue();
                            String title = documentSnapshot.getString("title");
                            Date creationDate = documentSnapshot.getTimestamp("creationDate").toDate();

                            Log.d(TAG, "onSuccess: ID of chapter " + chapterNumber + " is " + id);
                            chapters.add(new Chapter(id, chapterNumber, title, creationDate));
                        }

                        // Set up chapters recycler view
                        chapterListAdapter = new ChapterListAdapter(chapters, comicID);
                        chaptersRecyclerView.setAdapter(chapterListAdapter);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comic_details, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_favorite);

        isComicFavorited(comicID).addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    isComicFavorited = task.getResult();

                    if (isComicFavorited) {
                        menuItem.setIcon(R.drawable.ic_favorite_filled);
                    } else {
                        menuItem.setIcon(R.drawable.ic_favorite);
                    }
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite) {
            isComicFavorited = !isComicFavorited;

            if (isComicFavorited) {
                item.setIcon(R.drawable.ic_favorite_filled);
                addToFavorites(comicID);
            } else {
                item.setIcon(R.drawable.ic_favorite);
                removeFromFavorites(comicID);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Task<Boolean> isComicFavorited(String comicID) {
        String userID = auth.getCurrentUser().getUid();

        return firestore.collection("User")
                .document(userID)
                .get()
                .continueWith(new Continuation<DocumentSnapshot, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                List<String> favorites = (List<String>) documentSnapshot.get("favorites");

                                return favorites != null && favorites.contains(comicID);
                            }
                        }
                        return false;
                    }
                });
    }

    private void addToFavorites(String comicID) {
        String userID = auth.getCurrentUser().getUid();

        Map<String, Object> data = new HashMap<>();
        data.put("favorites", FieldValue.arrayUnion(comicID));

        firestore.collection("User")
                .document(userID)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Add comic " + comicID + " to Favorites.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: Failed to add comic " + comicID + " to Favorites.", e);
                    }
                });
    }

    private void removeFromFavorites(String comicID) {
        String userID = auth.getCurrentUser().getUid();

        firestore.collection("User")
                .document(userID)
                .update("favorites", FieldValue.arrayRemove(comicID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Remove comic " + comicID + " from Favorites.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: Failed to remove comic " + comicID + " from Favorites.", e);
                    }
                });
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
}