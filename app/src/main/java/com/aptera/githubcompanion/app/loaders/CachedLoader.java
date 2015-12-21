package com.aptera.githubcompanion.app.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.aptera.githubcompanion.lib.businesslogic.BusinessLogicException;
import com.aptera.githubcompanion.lib.data.Response;

/**
 * Created by daschliman on 12/17/2015.
 * Derived from https://github.com/rciovati/retrofit-loaders-example/blob/master/app/src/main/java/it/rciovati/testingretrofit/loader/RetrofitLoader.java
 */
public abstract class CachedLoader<TResult> extends AsyncTaskLoader<TResult> {

    private TResult mCachedResponse;

    protected void setCachedResponse(TResult cachedResponse) {
        mCachedResponse = cachedResponse;
    }
    protected TResult getCachedResponse() {
        return mCachedResponse;
    }

    public CachedLoader(Context context) {
        super(context);
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

}
