package com.codepath.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class PostActivity extends AppCompatActivity {
    private final static String TAG = "PostActivity";

    private Button btnSubmitPost;
    private Button btnTakePicture;
    private EditText etDescription;
    private ImageView ivImagePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Initialize the views when the post activity is launched.
        btnSubmitPost = findViewById(R.id.btnSubmitPost);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        etDescription = findViewById(R.id.etDescription);
        ivImagePost = findViewById(R.id.ivImagePost);

        // Create onClick listener for the "Take Picture" button.
        View.OnClickListener takePictureHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the camera/image gallery action to set the empty
                // image view to whatever the user selected.
                queryPosts();
            }
        };
        btnTakePicture.setOnClickListener(takePictureHandler);

        // Create onClick listener for the "Submit" button.
        View.OnClickListener submitPostHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Package the data by using the Parcel API into the intent
                // and go back to the main activity.

                // Grab the description, current user, and image.
                String description = etDescription.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();
                savePost(description, user);
                goToMainActvity();
            }
        };
        btnSubmitPost.setOnClickListener(submitPostHandler);
    }

    public void savePost(String description, ParseUser currentUser) {
        // Save the post to the database.
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    // Error while saving!
                    String errorMsg = "Error while saving the post.";
                    Toast.makeText(PostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    return;
                }
                String successMsg = "Post successful!";
                Toast.makeText(PostActivity.this, successMsg, Toast.LENGTH_SHORT).show();
                etDescription.setText("");
            }
        });

    }

    // Get the posts from the database.
    public void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> postList, ParseException e) {
                if (e != null) {
                    // A non-null parse exception means error when finding a particular post.
                    String errorMsg = "Error querying the post.";
                    Log.e(TAG, errorMsg);
                    return;
                }

                for (Post post: postList) {
                    Log.i(TAG, "Post description: " + post.getDescription());
                }
            }
        });
    }


    public void goToMainActvity() {
        Intent intent = new Intent(PostActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Getter methods for the post.
    public Button getBtnSubmitPost() {
        return btnSubmitPost;
    }

    public Button getBtnTakePicture() {
        return btnTakePicture;
    }

    public EditText getEtDescription() {
        return etDescription;
    }

    public ImageView getIvImagePost() {
        return ivImagePost;
    }
}