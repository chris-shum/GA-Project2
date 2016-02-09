package com.example.android.project;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {

    ProjectSQLiteOpenHelper mHelper;
    ImageView mFavoritesButton;
    WebView mWebView;
    View actionbar;
    String mFavoriteStatus;

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


        mHelper = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this);

        final int theIDNumber = getIntent().getIntExtra("id", -1);
        mFavoriteStatus = mHelper.getFavoritesById(theIDNumber);

        if (mFavoriteStatus.equals("0")) {
            mFavoritesButton.setImageResource(android.R.drawable.btn_star_big_off);
        } else {
            mFavoritesButton.setImageResource(android.R.drawable.btn_star_big_on);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFavoriteStatus.equals("0")) {
                    mFavoritesButton.setImageResource(android.R.drawable.btn_star_big_on);
                    mHelper.updateFavoriteStatus("1", theIDNumber);
                    mFavoriteStatus = mHelper.getFavoritesById(theIDNumber);
                } else {
                    mFavoritesButton.setImageResource(android.R.drawable.btn_star_big_off);
                    mHelper.updateFavoriteStatus("0", theIDNumber);
                    mFavoriteStatus = mHelper.getFavoritesById(theIDNumber);
                }
            }
        });


        TextView textViewDescription = (TextView) findViewById(R.id.textViewOnDetailsActivityDescription);

        if (theIDNumber >= 0) {
            String restaurantName = mHelper.getNameById(theIDNumber);
            String restaurantPrice = mHelper.getPriceById(theIDNumber);
            String restaurantType = mHelper.getTypeById(theIDNumber);
            String restaurantNeighborhood = mHelper.getNeighborhoodById(theIDNumber);
            String restaurantAddress = mHelper.getAddressById(theIDNumber);
            String restaurantDescription = mHelper.getDescriptionById(theIDNumber);
            String restaurantImage = mHelper.getImageById(theIDNumber);
            String restaurantReview = mHelper.getReviewById(theIDNumber);

            mWebView.loadUrl(restaurantImage);
            setTitle(restaurantName);
            String mainString = restaurantNeighborhood + "\n" + restaurantAddress + "\nFood type: " + restaurantType + "\nPrice: " + restaurantPrice + "\n\n" + restaurantDescription;
            textViewDescription.setText(mainString);
        }
    }
}
