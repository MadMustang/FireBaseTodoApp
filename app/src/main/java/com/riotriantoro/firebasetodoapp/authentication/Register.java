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
 * Registration Page
 * @author Rionaldy Triantoro
 */
public class Register extends AppCompatActivity {

    /** Register Username */
    private EditText userNameInput;

    /** Password input */
    private EditText passwordInput;

    /** Password confirmation */
    private EditText passwordConfirmation;

    /** Sign up button */
    private Button signUpButton;

    /** Sign in redirect */
    private Button signInRedirectButton;

    /** Registration progress bar */
    private ProgressBar registerProgressBar;

    /** Firebase Authentication */
    private FirebaseAuth mAuth;

    /** Log tag for register */
    private final static String LOG_TAG = "RegisterProcess";

    /**
     * @see AppCompatActivity#onCreate(Bundle, PersistableBundle) 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Attach widgets to associated variables
        userNameInput = findViewById(R.id.userNameInput);
        passwordInput = findViewById(R.id.inputPassword);
        passwordConfirmation = findViewById(R.id.confirmPassword);
        signUpButton = findViewById(R.id.submitRegister);
        signInRedirectButton = findViewById(R.id.redirectToSignIn);
        registerProgressBar = findViewById(R.id.progressBar);

        // Initialize FireBase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Assign logic to sign-up button
        signUpButton.setOnClickListener(new View.OnClickListener() {

            /**
             * @see android.view.View.OnClickListener#onClick(View)
             */
            @Override
            public void onClick(View v) {

                // Fetch data from input fields
                String userName = userNameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String confirmPass = passwordConfirmation.getText().toString().trim();

                // Validate not empty
                if (TextUtils.isEmpty(userName)) {
                    userNameInput.setError("Email cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordInput.setError("Password cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(confirmPass)) {
                    passwordConfirmation.setError("Password cannot be empty");
                    return;
                }

                // Validate password must be at least 6 characters
                if (password.length() < 6) {
                    passwordInput.setError("Password must be at least 6 characters");
                }

                // Validate passwords match
                if (!TextUtils.equals(password, confirmPass)) {
                    passwordConfirmation.setError("Password doesn't match");
                    return;
                }

                // Activate progress bar
                registerProgressBar.setVisibility(View.VISIBLE);

                // Check if user is signed-in
                if (mAuth.getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

                // Register to FireBase
                mAuth.createUserWithEmailAndPassword(userName, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            /**
                             * @see OnCompleteListener#onComplete(Task)
                             */
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(LOG_TAG, "Create user successful");
                                    Toast.makeText(Register.this, "User created",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),
                                            MainActivity.class));
                                    finish();
                                } else {
                                    String errorMessage = Objects.
                                            requireNonNull(task.getException()).getMessage();
                                    Log.d(LOG_TAG, "Create user failed. Error: " +
                                            errorMessage);
                                    Toast.makeText(Register.this, errorMessage,
                                            Toast.LENGTH_SHORT).show();
                                    registerProgressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            }
        });

        // Assign sign-in redirect
        signInRedirectButton.setOnClickListener(new View.OnClickListener() {

            /**
             * @see android.view.View.OnClickListener#onClick(View)
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }
}
