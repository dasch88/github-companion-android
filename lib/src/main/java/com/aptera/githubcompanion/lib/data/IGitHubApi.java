package com.aptera.githubcompanion.lib.data;

import com.aptera.githubcompanion.lib.model.Repository;
import com.aptera.githubcompanion.lib.model.User;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by daschliman on 12/16/2015.
 */
public interface IGitHubApi {
    @GET("/user")
    public User getAuthenticatedUser();
    @GET("/users/{user}")
    public User getUser(@Path("user") String user);
    @GET("/user/repos")
    public Repository[] getAuthenticatedUserRepositories();
    @GET("/users/{owner}/repos")
    public Repository[] getUserRepositories(@Path("owner") String owner);
    @GET("/repos/{owner}/{repo}")
    public Repository getRepository(@Path("owner") String owner, @Path("repo") String repo);
}
