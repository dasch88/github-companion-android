package com.aptera.githubcompanion.app.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.aptera.githubcompanion.lib.businesslogic.BusinessLogicException;
import com.aptera.githubcompanion.lib.data.Response;

/**
 * Created by daschliman on 12/17/2015.
 * Derived from https://github.com/rciovati/retrofit-loaders-example/blob/master/app/src/main/java/it/rciovati/testingretrofit/loader/RetrofitLoader.java
 */
public abstract class ResponseLoader<TResult> extends CachedLoader<Response<TResult>> {

    public ResponseLoader(Context context) {
        super(context);
    }

    @Override
    public Response<TResult> loadInBackground() {
        Response<TResult> response = null;
        try {
            final TResult data = performLoad();
            response = Response.ok(data);

        } catch (Exception ex) {
            response = Response.<TResult>error(ex);
            Log.e("Error", ex.getMessage());
            ex.printStackTrace();
        }
        setCachedResponse(response);

        return response;
    }
    protected abstract TResult performLoad() throws BusinessLogicException;

}
