package com.codepath.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    // Constants.
    private static final int MAX_NUMBER_OF_POST = 20;
    private static final String TAG = "FeedActivity";
    private static final String DATABASE_CREATEDAT = "createdAt";

    // Variables
    List<Post> postList;
    FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Load the recycler view for the user to see their feed.
        RecyclerView rvFeed = findViewById(R.id.rvFeed);
        postList = new ArrayList<>();

        feedAdapter = new FeedAdapter(FeedActivity.this, postList);
        rvFeed.setAdapter(feedAdapter);
        rvFeed.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
        queryPosts();

        // Set up an onRefreshListener.
        SwipeRefreshLayout swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Query the post and notify the adapter.
                queryPosts();

                // Signal the swipe container to stop visually refreshing.
                swipeContainer.setRefreshing(false);
            }
        });
    }

    // Get the posts from the database and updates the adapter's post list.
    public void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // We must include the user in our query since the
        // user is a nested ParseObject rather than a simple String or ParseFile.
        query.include(Post.KEY_USER);
        // Set a limit on the number of posts we query.
        query.setLimit(MAX_NUMBER_OF_POST);
        // Order the post by latest posts first.
        query.addDescendingOrder(DATABASE_CREATEDAT);

        // Finally, query in a background thread.
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> pL, ParseException e) {
                if (e != null) {
                    // A non-null parse exception means error when finding a particular post.
                    String errorMsg = "Error querying the post.";
                    Log.e(TAG, errorMsg);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : pL) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // Clear the post and notify the adapter.
                feedAdapter.clear();

                // Add the posts into the adapter and notify it.
                feedAdapter.addAll(pL);
            }
        });
    }


}