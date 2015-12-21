package com.aptera.githubcompanion.lib.data;

import com.aptera.githubcompanion.lib.model.User;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by daschliman on 12/16/2015.
 */
public interface IGitHubApi {
    @GET("/users/{user}")
    public User getUser(@Path("user") String user);
}
