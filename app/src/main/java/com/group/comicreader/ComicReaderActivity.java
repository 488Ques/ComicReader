package com.group.comicreader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group.comicreader.adapters.ComicPagerAdapter;

import java.util.List;

public class ComicReaderActivity extends AppCompatActivity {
    final String TAG = "ComicReaderActivity";

    private ViewPager2 viewPager;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_reader);

        // Get comicID and chapterID
        Intent intent = getIntent();
        String comicID = intent.getStringExtra("comicID");
        String chapterID = intent.getStringExtra("chapterID");
        Log.d(TAG, "onCreate: chapterID of comic " + comicID + " is " + chapterID);

        // Set up Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set up Authentication
        auth = FirebaseAuth.getInstance();

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_reader);
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

        firestore.collection("/Comic/" + comicID + "/Chapter").document(chapterID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> imageUrls = (List<String>) documentSnapshot.get("imageUrls");
                        String title = documentSnapshot.getString("title");

                        // Set title of toolbar
                        getSupportActionBar().setTitle(title);

                        // Set up ViewPager2
                        viewPager = findViewById(R.id.pager_reader_slider);
                        ComicPagerAdapter adapter = new ComicPagerAdapter(imageUrls);
                        viewPager.setAdapter(adapter);
                    }
                });
    }

    private void favoriteComic() {
        String uid = auth.getCurrentUser().getUid();
        firestore.collection("User").document(uid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comic_reader, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}