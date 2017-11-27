package com.frusoft.movier.util;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by nfrugoni on 9/10/17.
 */

abstract class BaseFetcherTask<Params,Progress,Result> extends AsyncTask<Params,Progress,Result> {
    private final AsyncTaskCompletionListener<Result> completionListener;
    protected final Context mContext;

    BaseFetcherTask(AsyncTaskCompletionListener<Result> completionListener, Context context) {
        this.completionListener = completionListener;
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        completionListener.OnFetchPreExecute();
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        completionListener.OnFetchCompleteWithResult(result);
    }
}
