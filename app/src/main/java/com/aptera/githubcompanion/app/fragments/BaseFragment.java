package com.aptera.githubcompanion.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.aptera.githubcompanion.app.GitHubCompanionApp;

/**
 * Created by daschliman on 12/29/2015.
 */
public class BaseFragment extends Fragment {

    public BaseFragment() {
        GitHubCompanionApp.getInstance().inject(this);
    }
}
