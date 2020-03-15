package com.riotriantoro.firebasetodoapp.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.riotriantoro.firebasetodoapp.MainActivity;
import com.riotriantoro.firebasetodoapp.R;

import java.util.Objects;

/**
 * Log-in Page
 * @author Rionaldy Triantoro
 */
public class Login extends AppCompatActivity {

    /** User email */
    private EditText userEmail;

    /** Password */
    private EditText password;

    /** SignIn Button */
    private Button signInButton;

    /** Redirect to signUp */
    private Button redirectToSignUpButton;

    /** Progress bar */
    private ProgressBar progressBar;

    /** Authentication */
    private FirebaseAuth mAuth;

    /** Log Tag */
    private final static String LOG_TAG = "LoginPage";

    /**
     * @see AppCompatActivity#onCreate(Bundle, PersistableBundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Attach widgets
        userEmail = findViewById(R.id.userEmailInput);
        password = findViewById(R.id.inputPasswordLogin);
        signInButton = findViewById(R.id.submitLogin);
        redirectToSignUpButton = findViewById(R.id.redirectToSignUp);
        progressBar = findViewById(R.id.progressBarLogin);

        // Initialize FireBase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Set listener for sign-in button
        signInButton.setOnClickListener(new View.OnClickListener() {

            /**
             * @see android.view.View.OnClickListener#onClick(View)
             */
            @Override
            public void onClick(View v) {

                // Fetch data from input fields
                String userName = userEmail.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();

                // Validate not empty
                if (TextUtils.isEmpty(userName)) {
                    userEmail.setError("Email cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(passwordStr)) {
                    password.setError("Password cannot be empty");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(userName, passwordStr)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            /**
                             * @see OnCompleteListener#onComplete(Task)
                             */
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(LOG_TAG, "Login Successful");
                                    Toast.makeText(Login.this, "Login Successful",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),
                                            MainActivity.class));
                                    finish();
                                } else {
                                    Log.d(LOG_TAG, "Login failed. Error: " +
                                            Objects.requireNonNull(task.getException()).getMessage());
                                    Toast.makeText(Login.this, "Login Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Assign redirect to signUp
        redirectToSignUpButton.setOnClickListener(new View.OnClickListener() {

            /**
             * @see android.view.View.OnClickListener#onClick(View)
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });
    }
}
