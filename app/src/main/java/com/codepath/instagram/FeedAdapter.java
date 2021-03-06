package com.codepath.instagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private List<Post> postList;
    private Context context;

    public FeedAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Assign a view holder containing a item_post.xml layout
        // to the activity_feed.xml layout.
        View viewPost = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolderToReturn = new ViewHolder(viewPost);

        // Create an on click listener for the view for viewing the details of a post.
        View.OnClickListener postClickedHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent, get the position of the clicked view of the post,
                // and start the post detail activity.
                int position = viewHolderToReturn.getAdapterPosition();
                Post clickedPost = postList.get(position);
                goToPostDetailActivity(clickedPost);
            }
        };
        viewPost.setOnClickListener(postClickedHandler);
        return viewHolderToReturn;
    }

    public void goToPostDetailActivity(Post post) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.TAG, post);
        context.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FeedAdapter.ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void clear() {
        postList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> pL) {
        postList.addAll(pL);
        notifyDataSetChanged();
    }

    // Inner class responsible for dynamically holding views.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvDescription;
        ImageView ivImagePost;

        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemPostView) {
            super(itemPostView);
            tvUsername = itemPostView.findViewById(R.id.tvUsername);
            tvDescription = itemPostView.findViewById(R.id.tvDescription);
            ivImagePost = itemPostView.findViewById(R.id.ivImagePostFeed);
        }

        public void bind(Post post) {
            // Bind the model data to the 3 components of the item_post view.
            tvUsername.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());

            // It is possible for a post to not have an image. In this case,
            // don't bind anything to the image view.
            ParseFile fileImagePost = post.getImage();
            if (fileImagePost != null) {
                Glide.with(context).load(fileImagePost.getUrl()).into(ivImagePost);
            }
        }

    }
}
