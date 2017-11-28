package com.frusoft.movier.activity;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frusoft.movier.model.MovieContract;
import com.frusoft.movier.util.MoviesDBHelper;

import static com.frusoft.movier.model.MovieContract.MovieEntry.COLUMN_FAV_DATE;
import static com.frusoft.movier.model.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.frusoft.movier.model.MovieContract.MovieEntry.MOVIES_TABLE_NAME;

/**
 * Created by nfrugoni on 18/11/17.
 */

public class MovieContentProvider extends ContentProvider {

    private MoviesDBHelper moviesDBHelper;

    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 101;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/*", MOVIES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        moviesDBHelper = new MoviesDBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor query;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                query = moviesDBHelper.getReadableDatabase().query(MOVIES_TABLE_NAME, null, null, null, null, null, COLUMN_FAV_DATE);
                break;
            case MOVIES_WITH_ID:
                String mSelection = COLUMN_MOVIE_ID + "=?";
                String[] mSelectionArgs = new String[]{uri.getPathSegments().get(1)};
                query = moviesDBHelper.getReadableDatabase().query(MOVIES_TABLE_NAME, null, mSelection, mSelectionArgs, null, null, COLUMN_FAV_DATE);
                break;
            default:
                throw new IllegalArgumentException("No query method for that URI");
        }

        if (query != null) {
            query.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return query;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = -1;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                id = moviesDBHelper.getWritableDatabase().insert(MOVIES_TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("No insert method for that URI");
        }
        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted = -1;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                if (selectionArgs != null && selectionArgs.length > 0) {
                    String[] whereArgs = {selectionArgs[0]};
                    String whereClause = COLUMN_MOVIE_ID + "=?";
                    deleted = moviesDBHelper.getWritableDatabase().delete(MOVIES_TABLE_NAME, whereClause, whereArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("No delete method for that URI");
        }
        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
