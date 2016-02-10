package com.example.android.project;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    ProjectSQLiteOpenHelper mHelper;
    ImageView mFavoritesButton;
    WebView mWebView;
    View actionbar;
    String mFavoriteStatus;
    TextView mTextViewDescription;
    RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFavoritesButton = (ImageView) findViewById(R.id.fab);
        mWebView = (WebView) findViewById(R.id.webViewTest);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        actionbar = findViewById(R.id.toolbar);
        mTextViewDescription = (TextView) findViewById(R.id.textViewOnDetailsActivityDescription);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);

        mHelper = ProjectSQLiteOpenHelper.getInstance(DetailsActivity.this);

        final int theIDNumber = getIntent().getIntExtra("id", -1);
        mFavoriteStatus = mHelper.getFavoritesById(theIDNumber);
        mRatingBar.setRating(Float.parseFloat((mHelper.getReviewById(theIDNumber))));
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#ffd700"), PorterDuff.Mode.SRC_ATOP);

        //the above gets the status of the favorite while below sets the image of the favorite star
        if (mFavoriteStatus.equals("0")) {
            mFavoritesButton.setImageResource(android.R.drawable.btn_star_big_off);
        } else {
            mFavoritesButton.setImageResource(android.R.drawable.btn_star_big_on);
        }

        //the actions pressing the star, changes the star image, edits database favorites column and notifies you of the change
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFavoriteStatus.equals("0")) {
                    mFavoritesButton.setImageResource(android.R.drawable.btn_star_big_on);
                    mHelper.updateFavoriteStatus("1", theIDNumber);
                    mFavoriteStatus = mHelper.getFavoritesById(theIDNumber);
                    Toast.makeText(DetailsActivity.this, mHelper.getNameById(theIDNumber) + " has been added to your favorites.", Toast.LENGTH_SHORT).show();
                } else {
                    mFavoritesButton.setImageResource(android.R.drawable.btn_star_big_off);
                    mHelper.updateFavoriteStatus("0", theIDNumber);
                    mFavoriteStatus = mHelper.getFavoritesById(theIDNumber);
                    Toast.makeText(DetailsActivity.this, mHelper.getNameById(theIDNumber) + " has been removed from your favorites.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //retrieves all the data and displays it
        if (theIDNumber >= 0) {
            String restaurantName = mHelper.getNameById(theIDNumber);
            String restaurantPrice = mHelper.getPriceById(theIDNumber);
            String restaurantType = mHelper.getTypeById(theIDNumber);
            String restaurantNeighborhood = mHelper.getNeighborhoodById(theIDNumber);
            String restaurantAddress = mHelper.getAddressById(theIDNumber);
            String restaurantDescription = mHelper.getDescriptionById(theIDNumber);
            String restaurantImage = mHelper.getImageById(theIDNumber);

            //webview grabs images from the net and loads it into my mWebView
            mWebView.loadUrl(restaurantImage);
            setTitle(restaurantName);
            String mainString = restaurantNeighborhood + "\n" + restaurantAddress + "\nFood type: " + restaurantType + "\nPrice: " + restaurantPrice + "\n\n" + restaurantDescription;
            mTextViewDescription.setText(mainString);

            //updates rating bar information on database when rating bar is clicked
            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    Log.d("Blah", String.valueOf(mRatingBar.getRating()));
                    mHelper.updateReviewStatus(Float.toString(mRatingBar.getRating()), theIDNumber);
                }
            });
        }
    }
}
