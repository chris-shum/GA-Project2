package com.example.android.project;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        Cursor cursor = mHelper.getFavoritesList();

        mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{ProjectSQLiteOpenHelper.COL_RESTAURANT_NAME}, new int[]{android.R.id.text1}, 0);
        mListViewResults.setAdapter(mCursorAdapter);


        mListViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mCursorAdapter.getCursor();
                Intent intent = new Intent(FavoritesActivity.this, ScrollingActivity.class);
                cursor.moveToPosition(position);
                int theIDNumber = cursor.getInt(cursor.getColumnIndex(ProjectSQLiteOpenHelper.COL_ID));
                intent.putExtra("id", theIDNumber);
                startActivity(intent);
            }
        });


    }

}

