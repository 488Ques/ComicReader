package com.group.comicreader;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group.comicreader.adapters.ChapterListAdapter;
import com.group.comicreader.models.Chapter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ComicDetailsActivity extends AppCompatActivity {
    private RecyclerView chaptersRecyclerView;
    private ChapterListAdapter chapterListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_details);

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
        TextView descriptionTextView = findViewById(R.id.text_details_description);
        ImageView coverImageView = findViewById(R.id.image_details_cover);
        chaptersRecyclerView = findViewById(R.id.recycler_details_chapters);

        // Set comic details
        titleTextView.setText("Chainsaw Man");
        authorTextView.setText("FUJIMOTO Tatsuki");
        genresTextView.setText("Action, Comedy, Drama, Horror, Mature, Shounen, Supernatural");
        descriptionTextView.setText("Meet Denji, he's poor. Like, sell your body parts poor. But he's found a gig killing demons with his chainsaw dog. Will it be enough to pay the bills?");
        Picasso.get().load(R.drawable.chainsaw_man).into(coverImageView);

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
}