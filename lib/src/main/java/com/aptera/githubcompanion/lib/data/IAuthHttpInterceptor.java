package com.aptera.githubcompanion.lib.data;

import com.aptera.githubcompanion.lib.utilities.IStateful;
import com.squareup.okhttp.Interceptor;

/**
 * Created by daschliman on 12/18/2015.
 */
public interface IAuthHttpInterceptor extends Interceptor, IStateful {
    void setCredentials(String username, String password);
    void clearCredentials();
}
