package com.aptera.githubcompanion.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;

import com.aptera.githubcompanion.R;
import com.aptera.githubcompanion.app.BaseActivity;
import com.aptera.githubcompanion.lib.businesslogic.IUserManager;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    @Inject
    public IUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //make sure that there is a currently logged in user. if not, load up the login activity
        if(userManager.getCurrentUser() == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

    }
}
