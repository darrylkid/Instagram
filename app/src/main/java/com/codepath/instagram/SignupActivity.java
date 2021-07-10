package com.codepath.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Setup on click listener for the sign up button.
        Button btnSignUpDetail = findViewById(R.id.btnSignupDetail);
        View.OnClickListener signUpHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the edit text fields.
                EditText etUsernameSignup = findViewById(R.id.etUsernameSignup);
                EditText etPasswordSignup = findViewById(R.id.etPasswordSignup);

                // Error handling for invalid user input.
                if (etUsernameSignup.getText() == null) {
                    String errorMsg = "Please enter an username.";
                    Toast.makeText(SignupActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }

                if (etPasswordSignup.getText() == null) {
                    String errorMsg = "Please enter an password.";
                    Toast.makeText(SignupActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }

                // Everything's good, proceed to signing up the user.
                String username = etUsernameSignup.getText().toString();
                String password = etPasswordSignup.getText().toString();
                signup(username, password);

            }
        };
        btnSignUpDetail.setOnClickListener(signUpHandler);
    }

        // Registers a new user with a user name and password into the Parse database.
        public void signup(String username, String password) {
            // Create the ParseUser
            ParseUser user = new ParseUser();

            // Set core properties
            user.setUsername(username);
            user.setPassword(password);

            // Invoke signUpInBackground
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Success with sign up.
                        Toast.makeText(SignupActivity.this, "Sign up successful!", Toast.LENGTH_LONG ).show();
                        goToMainActivity();
                    } else {
                        // Failure with sign up.
                        Log.e(TAG, "Sign up failed.", e);
                    }
                }
            });
        }

        public void goToMainActivity() {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


}