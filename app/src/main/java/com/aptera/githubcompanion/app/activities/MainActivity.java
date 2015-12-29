package com.aptera.githubcompanion.app.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aptera.githubcompanion.R;
import com.aptera.githubcompanion.app.fragments.IFragmentNavigationListener;
import com.aptera.githubcompanion.app.fragments.ITitled;
import com.aptera.githubcompanion.app.fragments.UserFragment;
import com.aptera.githubcompanion.lib.businesslogic.IUserManager;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements IFragmentNavigationListener {

    private ActionBarDrawerToggle mDrawerToggle;

    // UI references.
    private DrawerLayout mPnlDrawerLayout;
    private Toolbar mMainToolbar;

    @Inject
    public IUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make sure that there is a currently logged in user. if not, load up the login activity
        if(userManager.getCachedCurrentUser() == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        else {
            setContentView(R.layout.activity_main);

            mPnlDrawerLayout = (DrawerLayout) findViewById(R.id.pnlDrawerLayout);
            mMainToolbar = (Toolbar) findViewById(R.id.mainToolbar);

            //setup drawer
            setSupportActionBar(mMainToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            mDrawerToggle = new ActionBarDrawerToggle(this, mPnlDrawerLayout, mMainToolbar, R.string.drawer_open, R.string.drawer_close);
            mPnlDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mDrawerToggle.syncState();

            setActiveFragment(UserFragment.newInstance(userManager.getCachedCurrentUser().getLogin()));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void requestNavigation(Fragment frg) {

    }

    private void setActiveFragment(Fragment frg) {

        //determine new title
        int titleResourceId = R.string.app_name;
        if (frg != null && frg instanceof ITitled) {
            titleResourceId = ((ITitled) frg).getTitleResourceId();
        }
        String title = getResources().getString(titleResourceId);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        //perform the fragment change
        FragmentManager fMgr = getSupportFragmentManager();
        if(frg != null) {
            //swap out the new fragment

            fMgr.beginTransaction().replace(R.id.frmPlaceholder, frg).commit();
        }
        else {
            //remove the old fragment
            Fragment curr = fMgr.findFragmentById(R.id.frmPlaceholder);
            if(curr != null)
                fMgr.beginTransaction().remove(curr).commit();
        }
    }
}
