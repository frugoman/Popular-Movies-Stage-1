package com.frusoft.movier.util;

import android.os.AsyncTask;

/**
 * Created by nfrugoni on 9/10/17.
 */

abstract class BaseFetcherTask<Params,Progress,Result> extends AsyncTask<Params,Progress,Result> {
    private final AsyncTaskCompletionListener<Result> completionListener;

    BaseFetcherTask(AsyncTaskCompletionListener<Result> completionListener) {
        this.completionListener = completionListener;
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
