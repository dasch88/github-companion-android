package com.aptera.githubcompanion.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aptera.githubcompanion.R;

public class MainActivity extends AppCompatActivity {

    public final static String USERNAME_EXTRA = "com.aptera.githubcompanion.USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
