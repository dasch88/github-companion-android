package com.aptera.githubcompanion.app;

import com.aptera.githubcompanion.app.activities.LoginActivity;
import com.aptera.githubcompanion.app.activities.MainActivity;

import dagger.Module;

/**
 * Created by daschliman on 12/16/2015.
 */
@Module(
        injects = {
                MainActivity.class, LoginActivity.class
        },
        includes = {
                com.aptera.githubcompanion.lib.IocModule.class
        }
)
public class IocModule {

}
