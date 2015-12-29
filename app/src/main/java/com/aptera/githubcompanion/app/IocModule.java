package com.aptera.githubcompanion.app;

import com.aptera.githubcompanion.app.activities.LoginActivity;
import com.aptera.githubcompanion.app.activities.MainActivity;
import com.aptera.githubcompanion.app.fragments.RepositoryFragment;
import com.aptera.githubcompanion.app.fragments.UserFragment;

import dagger.Module;

/**
 * Created by daschliman on 12/16/2015.
 */
@Module(
        injects = {
                MainActivity.class, LoginActivity.class,
                RepositoryFragment.class, UserFragment.class
        },
        includes = {
                com.aptera.githubcompanion.lib.IocModule.class
        }
)
public class IocModule {

}
