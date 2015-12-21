package com.aptera.githubcompanion.lib.data;

import com.aptera.githubcompanion.lib.utilities.Base64;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by daschliman on 12/18/2015.
 */
public class BasicAuthHttpInterceptor implements Interceptor, IAuthHttpInterceptor {

    private String mAuthHeader;

    public void setCredentials(String username, String password) {
        if(username != null && password != null) {
            byte[] bytes = (username + ":" + password).getBytes();
            mAuthHeader = "Basic " + Base64.encodeBytes(bytes);
        }
        else {
            mAuthHeader = null;
        }
    }

    public void clearCredentials() {
        mAuthHeader = null;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request origRequest = chain.request();

        if(mAuthHeader != null) {
            Request newRequest = origRequest.newBuilder()
                    .header("Authorization", mAuthHeader)
                    .header("Accept", "application/json")
                    .method(origRequest.method(), origRequest.body()).build();

            return chain.proceed(newRequest);
        }

        return chain.proceed(origRequest);
    }
}
