package com.aptera.githubcompanion.app;

import android.app.Application;

import dagger.ObjectGraph;

/**
 * Created by daschliman on 12/16/2015.
 */
public class GitHubCompanionApp extends Application {

    private ObjectGraph objectGraph;

    @Override public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new IocModule());
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

}
