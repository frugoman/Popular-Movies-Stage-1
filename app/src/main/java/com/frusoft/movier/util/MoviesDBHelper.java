package com.frusoft.movier.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.frusoft.movier.model.MovieContract.MovieEntry.*;

/**
 * Created by nfrugoni on 18/11/17.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Movies.db";
    private static final int DATABASE_VERSION = 6;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + MOVIES_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                COLUMN_MOVIE_POSTER_URL + " TEXT NOT NULL, " +
                COLUMN_MOVIE_USER_RATING + " REAL NOT NULL, "+
                COLUMN_FAV_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String updateStatement = "DROP TABLE " + MOVIES_TABLE_NAME;
        db.execSQL(updateStatement);
        onCreate(db);
    }
}
