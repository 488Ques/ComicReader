package com.group.comicreader;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group.comicreader.adapters.ComicListAdapter;
import com.group.comicreader.models.Comic;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycler_comic_list;
    private ComicListAdapter comicListAdapter;
    private List<Comic> comicList;

    private void startSignIn() {
        ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        onSignInResult(result);
                    }
                }
        );

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();

        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        if (result.getResultCode() != Activity.RESULT_OK) {
            Log.e("MainActivity", response.getError().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_comic_list = findViewById(R.id.recycler_comic_list);
        recycler_comic_list.setHasFixedSize(true);
        recycler_comic_list.setLayoutManager(new GridLayoutManager(this, 2));

        comicList = new ArrayList<>();
        comicList.add(new Comic("Chainsaw Man", R.drawable.chainsaw_man));
        comicList.add(new Comic("Fire Punch", R.drawable.fire_punch));
        comicList.add(new Comic("Goodbye Eri", R.drawable.goodbye_eri));
        comicList.add(new Comic("Sakamoto Days", R.drawable.sakamoto_days));

        comicListAdapter = new ComicListAdapter(comicList);
        recycler_comic_list.setAdapter(comicListAdapter);
    }
}