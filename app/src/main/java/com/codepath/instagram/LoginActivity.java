package com.codepath.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // When the user is already logged in, simply go to the
        // main activity.
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity();
        }

        // When the login button is clicked, login.
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);

        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username, password);
            }
        });

        // On click listener for the sign up button.
        Button btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignupActivity();
            }
        });

    }

    public void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            // After login...
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // When the login is a failure, log an error message let nothing happen
                    // after the user clicks the login button.
                    String errorMsg = "Login failed.";
                    Log.e(TAG, errorMsg, e);
                    Toast.makeText(LoginActivity.this,
                                   errorMsg,
                                   Toast.LENGTH_SHORT).show();
                } else {
                    // When the login is a success, go to the main Instagram activity.
                    String successMsg = "Login successful.";
                    Toast.makeText(LoginActivity.this,
                                    successMsg,
                                    Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                }
            }
        });
    }

    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

        // Finish the login activity so that when the user goes back, the user doesn't
        // go back to the login activity.
        finish();
    }

    public void goToSignupActivity() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}