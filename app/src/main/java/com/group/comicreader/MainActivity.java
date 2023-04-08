package com.group.comicreader;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.group.comicreader.adapters.ComicListAdapter;
import com.group.comicreader.models.Comic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycler_comic_list;
    private ComicListAdapter comicListAdapter;
    private List<Comic> comicList;
    private ActivityResultLauncher<Intent> signInLauncher;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        onSignInResult(result);
                    }
                }
        );

        if (shouldStartSignIn()) {
            startSignIn();
        }

        recycler_comic_list = findViewById(R.id.recycler_comic_list);

        comicList = new ArrayList<>();
        comicList.add(new Comic("Chainsaw Man", R.drawable.chainsaw_man));
        comicList.add(new Comic("Fire Punch", R.drawable.fire_punch));
        comicList.add(new Comic("Goodbye Eri", R.drawable.goodbye_eri));
        comicList.add(new Comic("Sakamoto Days", R.drawable.sakamoto_days));

        comicListAdapter = new ComicListAdapter(comicList);
        recycler_comic_list.setAdapter(comicListAdapter);
    }

    private void startSignIn() {
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
            if (response == null) {
                // User pressed the back button
                finish();
            } else if (response.getError() != null && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                Toast.makeText(this, "No network", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean shouldStartSignIn() {
        return mAuth.getCurrentUser() == null;
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startSignIn();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                signOut();
                return true;
        }
        return false;
    }
}