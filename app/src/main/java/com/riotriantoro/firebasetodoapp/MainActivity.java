package com.riotriantoro.firebasetodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riotriantoro.firebasetodoapp.authentication.Login;

/**
 * Main application
 * @author Rionaldy Triantoro
 */
public class MainActivity extends AppCompatActivity {

    /** FireBase auth */
    private FirebaseAuth mAuth;

    private Button logout;

    private static final String LOG_TAG = "MainActivity";

    /**
     * @see AppCompatActivity#onCreate(Bundle, PersistableBundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ------------------------- Non default -----------------------------------
        Log.d(LOG_TAG, "Create main view");
        logout = findViewById(R.id.button);
        // Initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    /**
     * @see AppCompatActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "App Starting...");

        // Check if user is signed-in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d(LOG_TAG, "No user logged-in. Switching to log-in mode");
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    }
}
