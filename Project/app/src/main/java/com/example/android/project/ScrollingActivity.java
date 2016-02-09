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
    boolean mFavorites = false;
    ImageView mFavoritesButton;
    WebView mWebView;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mFavorites){
                    mFavoritesButton.setImageResource(android.R.drawable.btn_star_big_on);
                    mFavorites = true;
                }else{
                    mFavoritesButton.setImageResource(android.R.drawable.btn_star_big_off);
                    mFavorites = false;
                }
            }
        });

        mHelper = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this);

        int theIDNumber = getIntent().getIntExtra("id", -1);

        TextView textViewDescription = (TextView) findViewById(R.id.textViewOnDetailsActivityDescription);

        if (theIDNumber >= 0) {
            String restaurantName = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this).getNameById(theIDNumber);
            String restaurantPrice = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this).getPriceById(theIDNumber);
            String restaurantType = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this).getTypeById(theIDNumber);
            String restaurantNeighborhood = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this).getNeighborhoodById(theIDNumber);
            String restaurantAddress = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this).getAddressById(theIDNumber);
            String restaurantDescription = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this).getDescriptionById(theIDNumber);
            String restaurantImage = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this).getImageById(theIDNumber);
            String restaurantReview = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this).getReviewById(theIDNumber);
            String restaurantFavorite = ProjectSQLiteOpenHelper.getInstance(ScrollingActivity.this).getFavoritesById(theIDNumber);

            mWebView.loadUrl(restaurantImage);
            setTitle(restaurantName);

            String mainString = restaurantNeighborhood+"\n"+restaurantAddress+"\nFood type: "+restaurantType+"\nPrice: "+restaurantPrice+"\n\n"+restaurantDescription;

            textViewDescription.setText(mainString);

        }
    }
}
