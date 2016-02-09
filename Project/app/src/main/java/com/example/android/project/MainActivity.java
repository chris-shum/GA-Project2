package com.example.android.project;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.android.project.setup.DBAssetHelper;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewMain;
    private ListView mListViewResults;
    private ProjectSQLiteOpenHelper mHelper;
    private CursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ignore the two lines below, they are for setup
        DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
        dbSetup.getReadableDatabase();

        mTextViewMain = (TextView) findViewById(R.id.textViewOnMainActivityMainText);
        mListViewResults = (ListView) findViewById(R.id.listViewOnMainActivitySearchResults);
        mHelper = ProjectSQLiteOpenHelper.getInstance(MainActivity.this);

        mTextViewMain.setText("Welcome to Manhattan Eats!  " +
                "\nLet's find you a place to eat!" +
                "\nYou can browse through our restaurant list below," +
                "\nsearch above with the magnifying glass above," +
                "\nor check out your favorites also above." +
                "\n\nSearch by restaurant name, neighborhood, address, type of food, or price (cheap, moderate, or expensive)." +
                "\n\n\nRestaurant List:");

        //the below gets the data from database and adapter sets it to display on the listview.
        Cursor cursor = mHelper.getRestaurantList();
        mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{ProjectSQLiteOpenHelper.COL_RESTAURANT_NAME}, new int[]{android.R.id.text1}, 0);
        mListViewResults.setAdapter(mCursorAdapter);

        //searches and displays results
        handleIntent(getIntent());

        //onItemClickListener gets the ID# of the data being clicked, opens up details activity, and passes the ID# of the data.
        mListViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mCursorAdapter.getCursor();
                Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
                cursor.moveToPosition(position);
                int theIDNumber = cursor.getInt(cursor.getColumnIndex(ProjectSQLiteOpenHelper.COL_ID));
                intent.putExtra("id", theIDNumber);
                startActivity(intent);
            }
        });
    }

    //search settings setup as shown in lesson
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    //makes the favorites button in the toolbar functional, opens favorites intent when button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favoritesButton:
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    //all the functions of the search and refreshing the list with the results
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Cursor cursor = mHelper.searchRestaurantList(query);
            mCursorAdapter.changeCursor(cursor);
            if (cursor.getCount() == 0) {
                mTextViewMain.setText("Your search results for \"" + query + "\"yielded no results.");
            } else {
                mTextViewMain.setText("Search results for \"" + query + "\":");
            }
        }
    }
}
