package com.aptera.githubcompanion.app.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

}
