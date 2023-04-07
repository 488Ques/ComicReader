package com.group.comicreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.group.comicreader.adapters.ComicPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ComicReaderActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private boolean mIsFullscreen = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_reader);

        // Get comic data from intent
        // Intent intent = getIntent();
        // Comic comic = intent.getParcelableExtra("comic");

        // Set toolbar title to comic title
        Toolbar toolbar = findViewById(R.id.toolbar_reader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chapter 1");

        // Sample data
        List<Integer> pages = new ArrayList<>();
        pages.add(R.drawable.chainsaw_man);
        pages.add(R.drawable.fire_punch);

        // Set up ViewPager2
        viewPager = findViewById(R.id.pager_reader_slider);
        ComicPagerAdapter adapter = new ComicPagerAdapter(pages);
        viewPager.setAdapter(adapter);


//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    Log.d("ComicReader", "TOUCHED DOWN");
//                    float x = event.getX();
//                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
//
//                    if (x < screenWidth / 2) {
//                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
//                    } else {
//                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 2, true);
//                    }
//                }
//                return false;
//            }
//        });

        // Hide UI elements when tapping middle of screen

    }

    private void toggleFullscreen() {
        mIsFullscreen = !mIsFullscreen;
        Window window = getWindow();
        if (mIsFullscreen) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().show();
        }
    }
}