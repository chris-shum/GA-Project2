package com.example.android.project.setup;


import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by ShowMe on 2/5/16.
 */
public class DBAssetHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "RESTAURANTS_DB";
    private static final int DATABASE_VERSION = 7;

    public DBAssetHelper(Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
