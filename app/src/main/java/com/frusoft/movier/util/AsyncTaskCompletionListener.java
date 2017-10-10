package com.frusoft.movier.util;

/**
 * Created by nfrugoni on 9/10/17.
 */

public interface AsyncTaskCompletionListener<T> {

    void OnFetchPreExecute();

    void OnFetchCompleteWithResult(T result);
}
