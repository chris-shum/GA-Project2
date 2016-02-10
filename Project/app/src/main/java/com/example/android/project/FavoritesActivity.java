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
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.android.project.setup.DBAssetHelper;

public class FavoritesActivity extends AppCompatActivity {

    private TextView mTextViewMain;
    private ListView mListViewResults;
    private ProjectSQLiteOpenHelper mHelper;
    private CursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //Ignore the two lines below, they are for setup
        DBAssetHelper dbSetup = new DBAssetHelper(FavoritesActivity.this);
        dbSetup.getReadableDatabase();

        mTextViewMain = (TextView) findViewById(R.id.textViewOnFavoritesActivityMainText);
        mListViewResults = (ListView) findViewById(R.id.listViewOnFavoritesActivity);
        mHelper = ProjectSQLiteOpenHelper.getInstance(FavoritesActivity.this);

        //gets favorites list (all items in column with "1" in column) and displays on listview
        Cursor cursor = mHelper.getFavoritesList();
        mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{ProjectSQLiteOpenHelper.COL_RESTAURANT_NAME}, new int[]{android.R.id.text1}, 0);
        mListViewResults.setAdapter(mCursorAdapter);

        //searches and displays results
        handleIntent(getIntent());

        //onItemClickListener gets the ID# of the data being clicked, opens up details activity, and passes the ID# of the data.
        mListViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mCursorAdapter.getCursor();
                Intent intent = new Intent(FavoritesActivity.this, DetailsActivity.class);
                cursor.moveToPosition(position);
                int theIDNumber = cursor.getInt(cursor.getColumnIndex(ProjectSQLiteOpenHelper.COL_ID));
                intent.putExtra("id", theIDNumber);
                startActivity(intent);
            }
        });
    }

    //had an update error when pressing back buttons after changing a favorite status in page, this refreshes to update the list to the latest changes upon opening
    //if else statement was added when search function was added because list wasn't updating
    @Override
    public void onResume() {
        super.onResume();
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
        } else {
            Cursor cursor = mHelper.getFavoritesList();
            mCursorAdapter.changeCursor(cursor);
        }
//        mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{ProjectSQLiteOpenHelper.COL_RESTAURANT_NAME}, new int[]{android.R.id.text1}, 0);
        //mListViewResults.setAdapter(mCursorAdapter);
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

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    //all the functions of the search and refreshing the list with the results
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Cursor cursor = mHelper.searchRestaurantListFromFavorites(query);
            mCursorAdapter.changeCursor(cursor);

            if (cursor.getCount() == 0) {
                mTextViewMain.setText("Your search results for \"" + query + "\"yielded no results.");
            } else {
                mTextViewMain.setText("Search results for \"" + query + "\":");
            }
        }
    }
}

