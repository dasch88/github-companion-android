package com.aptera.githubcompanion.lib.data;

import java.util.concurrent.Callable;

/**
 * Created by daschliman on 12/17/2015.
 * Slightly changed from https://github.com/rciovati/retrofit-loaders-example/blob/master/app/src/main/java/it/rciovati/testingretrofit/loader/Response.java
 */
public class Response<T> {

    private Exception mException;
    private T mResult;

    public static <T> Response<T> ok(T data){

        Response<T> response = new Response<T>();
        response.mResult = data;

        return  response;
    }

    public static <T> Response<T> error(Exception ex){

        Response<T> response = new Response<T>();
        response.mException = ex;

        return  response;
    }

    public boolean hasError() {

        return mException != null;
    }

    public Exception getException() {

        return mException;
    }

    public T getResult() {

        return mResult;
    }
}