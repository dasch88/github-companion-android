package com.aptera.githubcompanion.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aptera.githubcompanion.app.GitHubCompanionApp;
import com.aptera.githubcompanion.lib.utilities.IStateful;
import com.aptera.githubcompanion.lib.utilities.IStatefulRegistry;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by daschliman on 12/16/2015.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String STATEFUL_REGISTRY_PREFIX = "StReg_";

    @Inject
    public IStatefulRegistry statefulRegistry;

    public BaseActivity() {
        GitHubCompanionApp.getInstance().inject(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //save all stateful information in the registry
        for(Map.Entry<String, IStateful> entry : statefulRegistry.getEntries()) {
            savedInstanceState.putSerializable(STATEFUL_REGISTRY_PREFIX + entry.getKey(), entry.getValue().getState());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //restore all stateful information in the registry that starts with the prefix
        for(String key : savedInstanceState.keySet()) {
            if(key.startsWith(STATEFUL_REGISTRY_PREFIX)) {
                String actualKey = key.substring(STATEFUL_REGISTRY_PREFIX.length());
                IStateful stateful = statefulRegistry.getEntry(actualKey);
                if(stateful != null) {
                    stateful.restoreState(savedInstanceState.getSerializable(key));
                }
            }
        }
    }
}
