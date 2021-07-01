package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.w3c.dom.Text;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    public interface OnClickListener {
        void onLikeClicked(int position);
        void onRetweetClicked(int position);
        void onReplyClicked(int position);
    }

    public static final String TAG = "TweetsAdapter";

    Context context;
    List<Tweet> tweets;
    OnClickListener clickListener;

    public TweetsAdapter(Context context, List<Tweet> tweets, OnClickListener clickListener) {
        this.context = context;
        this.tweets = tweets;
        this.clickListener = clickListener;
    }

    // Inflates layout from XML and return it inside of View Holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View tweetView = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(tweetView);
    }

    // Populating data into the item through the View Holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder " + position);
        // Get the movie at the position
        Tweet tweet = tweets.get(position);
        // Bind the movie data into the View Holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }


    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvName;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvCreatedAt;
        ImageView ivMediaImage;
        ImageButton btnLike;
        ImageButton btnRetweet;
        ImageButton btnReply;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            ivMediaImage = itemView.findViewById(R.id.ivMediaImage);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            btnReply = itemView.findViewById(R.id.btnReply);

        }

        // Binding the movie objects to the created cells when scrolling
        public void bind(final Tweet tweet) {
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            tvScreenName.setText("@"+tweet.user.screenName);
            tvCreatedAt.setText("• " + ParseRelativeDate.getRelativeTimeAgo(tweet.createdAt));
            Glide.with(context).load(tweet.user.profileImageUrl)
                    .transform(new CenterCrop(), new RoundedCorners(100))
                    .into(ivProfileImage);
            if (tweet.mediaUrl != null) {
                ivMediaImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.mediaUrl)
                        .transform(new CenterCrop(), new RoundedCorners(20))
                        .into(ivMediaImage);
            } else {
                ivMediaImage.setVisibility(View.GONE);
                Glide.with(context).clear(ivMediaImage);
            }

            if (tweet.liked){
                btnLike.setBackgroundResource(R.drawable.ic_vector_heart);
            } else {
                btnLike.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
            }

            if (tweet.retweeted){
                btnRetweet.setBackgroundResource(R.drawable.ic_vector_retweet);
            } else {
                btnRetweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
            }

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onLikeClicked(getAdapterPosition());
                }
            });

            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onRetweetClicked(getAdapterPosition());
                }
            });

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onReplyClicked(getAdapterPosition());
                }
            });

            Log.i("Entity", "Media Exists " + tweet.mediaUrl);
        }

    }

}
