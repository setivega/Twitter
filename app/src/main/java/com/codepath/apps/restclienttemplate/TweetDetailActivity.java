package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {

    public static final String TAG = "TweetDetailActivity";

    Tweet tweet;

    private TwitterClient client;
    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvScreenName;
    private TextView tvBody;
    private TextView tvCreatedAt;
    private ImageView ivMediaImage;
    private ImageButton btnLike;
    private ImageButton btnRetweet;
    private TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        // Setting our variables equal to their xml counterparts
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvName);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvBody = findViewById(R.id.tvBody);
        ivMediaImage = findViewById(R.id.ivMediaImage);
        btnLike = findViewById(R.id.btnLike);
        btnRetweet = findViewById(R.id.btnRetweet);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);

        // Creating our client variable
        client = TwitterApp.getRestClient(this);

        // unwrap tweet
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvName.setText(tweet.user.name);
        tvScreenName.setText("@"+tweet.user.screenName);
        tvCreatedAt.setText("• " + ParseRelativeDate.getRelativeTimeAgo(tweet.createdAt));
        tvBody.setText(tweet.body);

        Glide.with(ivProfileImage.getContext()).load(tweet.user.profileImageUrl)
                .transform(new CenterCrop(), new RoundedCorners(100))
                .into(ivProfileImage);

        // Determining if media exists and the loading it into the image view if it does
        if (tweet.mediaUrl != null) {
            ivMediaImage.setVisibility(View.VISIBLE);
            Glide.with(ivMediaImage.getContext()).load(tweet.mediaUrl)
                    .transform(new CenterCrop(), new RoundedCorners(20))
                    .into(ivMediaImage);
        } else {
            ivMediaImage.setVisibility(View.GONE);
            Glide.with(ivMediaImage.getContext()).clear(ivMediaImage);
        }

        // Setting the correct images for the images buttons
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

    }
}