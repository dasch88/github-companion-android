package com.aptera.githubcompanion.app.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.ImageView;

import com.aptera.githubcompanion.lib.data.Response;

import java.io.InputStream;

/**
 * Created by daschliman on 12/21/2015.
 */
public class BitmapLoader extends CachedLoader<Bitmap> {

    private String mUrl;

    public BitmapLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public Bitmap loadInBackground() {
        Bitmap response = null;
        try {
            InputStream in = new java.net.URL(mUrl).openStream();
            response = BitmapFactory.decodeStream(in);
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        setCachedResponse(response);

        return response;
    }

    public static LoaderManager.LoaderCallbacks<Bitmap> createImageLoaderCallbacks(final Context ctx, final ImageView imgView, final String imageUrl) {
        return new LoaderManager.LoaderCallbacks<Bitmap>() {

            @Override
            public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
                return new BitmapLoader(ctx, imageUrl);
            }

            @Override
            public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
                imgView.setImageBitmap(data);
            }

            @Override
            public void onLoaderReset(Loader<Bitmap> loader) {

            }
        };
    }
}
