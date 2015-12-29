package com.aptera.githubcompanion.app;

import android.app.Application;

import dagger.ObjectGraph;

/**
 * Created by daschliman on 12/16/2015.
 */
public class GitHubCompanionApp extends Application {

    private ObjectGraph mObjectGraph;
    private static GitHubCompanionApp mInstance;

    public static GitHubCompanionApp getInstance() {
        return mInstance;
    }


    @Override public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(new IocModule());
        mInstance = this;
    }

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

}
