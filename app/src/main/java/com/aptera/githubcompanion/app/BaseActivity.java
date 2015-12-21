package com.aptera.githubcompanion.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by daschliman on 12/16/2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ((GitHubCompanionApp) getApplication()).inject(this);
    }
}
