package com.codepath.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    // Constants
    private final static String TAG = "PostActivity";
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    // Views
    private Button btnSubmitPost;
    private Button btnTakePicture;
    private EditText etDescription;
    private ImageView ivImagePost;

    // Camera
    private File photoFile;
    private String photoFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Initialize the views when the post activity is launched.
        btnSubmitPost = findViewById(R.id.btnSubmitPost);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        etDescription = findViewById(R.id.etDescription);
        ivImagePost = findViewById(R.id.ivImagePost);

        // Initialize the camera-related variables.
        photoFile = null;
        photoFileName = "photo.jpg";


        // Create onClick listener for the "Take Picture" button.
        View.OnClickListener takePictureHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the camera/image gallery action to set the empty
                // image view to whatever the user selected.
                launchCamera();
            }
        };
        btnTakePicture.setOnClickListener(takePictureHandler);

        // Create onClick listener for the "Submit" button.
        View.OnClickListener submitPostHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab the description, current user, and photo file (image provided
                // by user when the "Take Picture" button is clicked).
                String description = etDescription.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();

                // Error handling for an invalid description.
                if (etDescription.getText().toString().isEmpty()) {
                    String errorMsg = "No description."
                                    +  " Please add a description";
                    Toast.makeText(PostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Error handling for a user submitting a post with no photo.
                if (photoFile == null || ivImagePost.getDrawable() == null) {
                    String errorMsg = "No picture. Please take a picture.";
                    Toast.makeText(PostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the post to the database.
                savePost(description, user, photoFile);
                goToMainActvity();
            }
        };
        btnSubmitPost.setOnClickListener(submitPostHandler);
    }

    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.e(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivImagePost.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void savePost(String description, ParseUser currentUser, File photoFile) {
        // Save the post to the database.
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        post.setImage(new ParseFile(photoFile));
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

                // Clear the edit text view description and image view.
                etDescription.setText("");
                ivImagePost.setImageResource(0);
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