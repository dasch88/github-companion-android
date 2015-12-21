package com.aptera.githubcompanion.lib;

import com.aptera.githubcompanion.lib.data.BasicAuthHttpInterceptor;
import com.aptera.githubcompanion.lib.data.IAuthHttpInterceptor;
import com.aptera.githubcompanion.lib.data.IGitHubApi;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by daschliman on 12/16/2015.
 */
@Module(complete = true, library = true)
public class IocModule {
    @Provides
    @Singleton
    public IGitHubApi provideGitHubApi(IAuthHttpInterceptor authInterceptor) {
        String api = "https://api.github.com";

        //build network interceptor to add in authentication
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.networkInterceptors().add(authInterceptor);

        //build rest adapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(api)
                .setClient(new OkClient(httpClient))
                .build();
        return adapter.create(IGitHubApi.class);
    }
    @Provides
    @Singleton
    public IAuthHttpInterceptor provideAuthHttpInterceptor() {
        return new BasicAuthHttpInterceptor();
    }
}
