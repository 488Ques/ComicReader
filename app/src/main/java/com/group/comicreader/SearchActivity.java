package com.group.comicreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group.comicreader.adapters.ComicListAdapter;
import com.group.comicreader.models.ComicListItem;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private final String TAG = "SearchActivity";
    private RecyclerView recyclersearchcomic;
    private List<ComicListItem> comicList_search;
    private FirebaseFirestore firestore;
    private ComicListAdapter comicListAdapter;
    private EditText editText_search;
    private Button button_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Find views
        recyclersearchcomic = findViewById(R.id.recycler_search_comic);
        editText_search = findViewById(R.id.edittext_search);
        button_cancel = findViewById(R.id.button_cancel);

        // Set up Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set up adapter and recycler
        comicListAdapter = new ComicListAdapter();
        recyclersearchcomic.setAdapter(comicListAdapter);

        // Handle cancel button's event
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Handle the done button click event on the keyboard after data entry is done in edittext
        editText_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    //hide keyboard when done . button is pressed
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);

                    //Search comics by query and load comics in Recycler view
                    searchComic(editText_search.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }
    private void searchComic(String query) {
        comicList_search = new ArrayList<>();
        CollectionReference comicRef = firestore.collection("Comic");
        Query searchQuery = comicRef.whereGreaterThanOrEqualTo("title", query)
                .whereLessThanOrEqualTo("title", query + "\uf8ff");

        searchQuery.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
                            {
                                String id = queryDocumentSnapshot.getId();
                                String title = queryDocumentSnapshot.getString("title");
                                String imageUrl = queryDocumentSnapshot.getString("imageUrl");
                                comicList_search.add(new ComicListItem(id,title,imageUrl));
                            }
                            comicListAdapter.setComicList(comicList_search);
                            Log.d(TAG, "onSuccess: Found");

                        } else {
                            Toast.makeText(SearchActivity.this, " No results found for keyword \""+query+"\"", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onSuccess: No result");
                        }
                    }
                });
    }

}