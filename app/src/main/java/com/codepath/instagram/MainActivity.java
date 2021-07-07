package com.codepath.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an onClick listener for the log out button.
        Button btnLogout = findViewById(R.id.btnLogout);
        View.OnClickListener logoutHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Log.i(TAG, "Logout successful");
                goToLoginActivity();
            }
        };
        btnLogout.setOnClickListener(logoutHandler);

        // Create an onClick listener for the post button.
        Button btnCreatePost = findViewById(R.id.btnCreatePost);
        View.OnClickListener createPostHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When button is clicked, go to the Post activity
                // to create a new post.
                goToPostActivity();
            }
        };
        btnCreatePost.setOnClickListener(createPostHandler);

    }


    public void goToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToPostActivity() {
        Intent intent = new Intent(MainActivity.this, Post.class);
        startActivity(intent);
        // We don't include finish here so that the user can go back to the
        // main activity.
    }
}