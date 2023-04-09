package com.group.comicreader;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group.comicreader.adapters.ChapterListAdapter;
import com.group.comicreader.models.Chapter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ComicDetailsActivity extends AppCompatActivity {
    final int MAX_LINES = 3;

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

        Button showMoreButton = findViewById(R.id.button_details_show_more);
        ImageView coverImageView = findViewById(R.id.image_details_cover);
        chaptersRecyclerView = findViewById(R.id.recycler_details_chapters);

        // Set comic details
        titleTextView.setText("Chainsaw Man");
        authorTextView.setText("FUJIMOTO Tatsuki");
        genresTextView.setText("Action, Comedy, Drama, Horror, Mature, Shounen, Supernatural");
        descriptionTextView.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed fringilla justo sit amet nisi malesuada, nec faucibus magna venenatis." +
                "nec faucibus magna venenatis. nec faucibus magna venenatis. nec faucibus magna venenatis.");
        Picasso.get().load(R.drawable.chainsaw_man).into(coverImageView);

        // Add sample chapters
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(new Chapter(1, "Chapter 1 Title", "2022-04-01"));
        chapters.add(new Chapter(2, "Chapter 2 Title", "2022-04-15"));
        chapters.add(new Chapter(3, "Chapter 3 Title", "2022-05-01"));

        // Show or hide Show more button depending on whether description text is longer than 3 lines
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

        // Set up chapters recycler view
        chapterListAdapter = new ChapterListAdapter(chapters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chaptersRecyclerView.setLayoutManager(layoutManager);
        chaptersRecyclerView.setAdapter(chapterListAdapter);
    }
}