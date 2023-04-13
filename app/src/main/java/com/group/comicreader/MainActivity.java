package com.group.comicreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private ActivityResultLauncher<Intent> signInLauncher;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Set up authentication stuff
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

        // Start signing in if eligible
        if (shouldStartSignIn()) {
            startSignIn();
        }

        // Handle bottom navigation view event
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnav_main);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                String fragmentTag = null;
                switch (item.getItemId()) {
                    case R.id.bottomnav_home:
                        fragmentTag = "HomeFragment"; // set a unique tag for HomeFragment
                        break;
                    case R.id.bottomnav_favorites:
                        fragmentTag = "FavoritesFragment"; // set a unique tag for FavoritesFragment
                        break;
                }

                // Check if the fragment is already added to the fragment_container
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment existingFragment = fragmentManager.findFragmentByTag(fragmentTag);
                if (existingFragment != null) {
                    // If fragment already exists, use it
                    selectedFragment = existingFragment;
                } else {
                    // If fragment does not exist, create a new instance
                    switch (item.getItemId()) {
                        case R.id.bottomnav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.bottomnav_favorites:
                            selectedFragment = new FavoritesFragment();
                            break;
                    }
                }

                // Replace the fragment in the fragment_container
                if (selectedFragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment, fragmentTag).commit();
                    return true;
                }

                return false;
            }
        });

        // Manually select the default item
        bottomNavigationView.setSelectedItemId(R.id.bottomnav_home);
    }

    // Launch sign in sequence
    private void startSignIn() {
        // Only use email for authentication
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

    // Handle results of sign in
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

    // Check if eligible for signing out
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

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchComic(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void searchComic(String query) {
        CollectionReference comicRef = FirebaseFirestore.getInstance().collection("Comic");
//        Query searchQuery = comicRef.whereEqualTo("title", query);
        Query searchQuery = comicRef.whereGreaterThanOrEqualTo("title", query)
                .whereLessThanOrEqualTo("title", query + "\uf8ff");

        searchQuery.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: Found");
                        } else {
                            Log.d(TAG, "onSuccess: No result");
                        }
                    }
                });
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