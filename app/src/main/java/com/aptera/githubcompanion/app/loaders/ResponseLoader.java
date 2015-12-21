package com.aptera.githubcompanion.app.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.aptera.githubcompanion.lib.businesslogic.BusinessLogicException;
import com.aptera.githubcompanion.lib.data.Response;

/**
 * Created by daschliman on 12/17/2015.
 * Derived from https://github.com/rciovati/retrofit-loaders-example/blob/master/app/src/main/java/it/rciovati/testingretrofit/loader/RetrofitLoader.java
 */
public abstract class ResponseLoader<TResult> extends AsyncTaskLoader<Response<TResult>> {

    private Response<TResult> mCachedResponse;

    public ResponseLoader(Context context) {

        super(context);
    }

    @Override
    public Response<TResult> loadInBackground() {

        try {

            final TResult data = performLoad();
            mCachedResponse = Response.ok(data);

        } catch (Exception ex) {

            mCachedResponse = Response.error(ex);
        }

        return mCachedResponse;
    }

    @Override
    protected void onStartLoading() {

        super.onStartLoading();

        if (mCachedResponse != null) {

            deliverResult(mCachedResponse);
        }

        if (takeContentChanged() || mCachedResponse == null) {

            forceLoad();
        }
    }

    @Override
    protected void onReset() {

        super.onReset();

        mCachedResponse = null;
    }

    protected abstract TResult performLoad() throws BusinessLogicException;

}
