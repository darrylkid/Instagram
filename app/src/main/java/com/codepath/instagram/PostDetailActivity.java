package com.codepath.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;

public class PostDetailActivity extends AppCompatActivity {
    public static final String TAG = "PostDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Post unwrappedPost = getIntent().getParcelableExtra(TAG);
        bindDetail(unwrappedPost);
    }

    //
    public void bindDetail(Post post) {
        // Get all the views inside the Post Detail activity.
        TextView tvUsernameDetail = findViewById(R.id.tvUsernameDetail);
        TextView tvCreatedAt = findViewById(R.id.tvCreatedAt);
        TextView tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
        ImageView ivImagePostDetail = findViewById(R.id.ivImagePostDetail);

        // Bind data to the views.
        tvUsernameDetail.setText(post.getUser().getUsername());
        tvCreatedAt.setText(calculateTimeAgo(post.getCreatedAt()));
        tvDescriptionDetail.setText(post.getDescription());

        // It is possible for a post to not have an image. In this case,
        // don't bind anything to the image view.
        ParseFile fileImagePost = post.getImage();
        if (fileImagePost != null) {
            Glide.with(this).load(fileImagePost.getUrl()).into(ivImagePostDetail);
        }

    }

    // Converts a date object to a human-readable time.
    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }
}
