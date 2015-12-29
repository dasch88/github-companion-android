package com.aptera.githubcompanion.app.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aptera.githubcompanion.R;
import com.aptera.githubcompanion.app.adapters.FragmentNavigationAdapter;
import com.aptera.githubcompanion.app.fragments.IFragmentNavigationListener;
import com.aptera.githubcompanion.app.fragments.ITitled;
import com.aptera.githubcompanion.app.fragments.UserFragment;
import com.aptera.githubcompanion.app.loaders.BitmapLoader;
import com.aptera.githubcompanion.app.viewmodels.FragmentNavigationMapping;
import com.aptera.githubcompanion.lib.businesslogic.IUserManager;
import com.aptera.githubcompanion.lib.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements IFragmentNavigationListener {

    private static final int PROFILE_IMAGE_LOADER_ID = 0;

    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentNavigationAdapter mNavAdapter;

    // UI references.
    private ImageView mImgDrawerProfileImage;
    private TextView mTxtDrawerUserLogin;
    private TextView mTxtDrawerUserFullName;
    private DrawerLayout mPnlDrawerLayout;
    private RelativeLayout mPnlNavigationDrawer;
    private Toolbar mMainToolbar;
    private ListView mLstDrawerNavigationPages;

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

            mImgDrawerProfileImage = (ImageView) findViewById(R.id.imgDrawerProfileImage);
            mTxtDrawerUserLogin = (TextView) findViewById(R.id.txtDrawerUserLogin);
            mTxtDrawerUserFullName = (TextView) findViewById(R.id.txtDrawerUserFullName);
            mPnlDrawerLayout = (DrawerLayout) findViewById(R.id.pnlDrawerLayout);
            mPnlNavigationDrawer = (RelativeLayout) findViewById(R.id.pnlNavigationDrawer);
            mMainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
            mLstDrawerNavigationPages = (ListView) findViewById(R.id.lstDrawerNavigationPages);

            //setup drawer
            setSupportActionBar(mMainToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            mNavAdapter = new FragmentNavigationAdapter(this, R.layout.listitem_selectable_navigation, createNavigationMappings());
            mLstDrawerNavigationPages.setAdapter(mNavAdapter);
            mLstDrawerNavigationPages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentNavigationMapping navMapping = mNavAdapter.getItem(position);
                    selectNavigation(navMapping);
                }
            });

            mDrawerToggle = new ActionBarDrawerToggle(this, mPnlDrawerLayout, mMainToolbar, R.string.drawer_open, R.string.drawer_close);
            mPnlDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();

            //set default page and load profile box
            loadProfileBox();
            selectNavigation(mNavAdapter.getItem(0));
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
        setActiveFragment(frg);
    }

    private void loadProfileBox() {
        User user = userManager.getCachedCurrentUser();
        //load profile image
        getSupportLoaderManager().initLoader(PROFILE_IMAGE_LOADER_ID, null, BitmapLoader.createImageLoaderCallbacks(this, mImgDrawerProfileImage, user.getAvatarUrl()));

        //load name fields
        mTxtDrawerUserLogin.setText(user.getLogin());
        mTxtDrawerUserFullName.setText(user.getName());
    }

    private void selectNavigation(FragmentNavigationMapping navMapping) {
        setNavigationHighlighting(navMapping.displayTextId);
        mPnlDrawerLayout.closeDrawer(mPnlNavigationDrawer);
        Fragment frg = navMapping.createDefaultFragment();
        if(frg == null)
            handleNullNavigation(navMapping.displayTextId);
        else
            setActiveFragment(frg);
    }
    private void setNavigationHighlighting(int displayTextId) {
        //set all items to isSelected false, then true for item here
        for (int i = 0; i < mNavAdapter.getCount(); i++) {
            FragmentNavigationMapping mapping = mNavAdapter.getItem(i);
            if(mapping != null)
                mapping.isSelected = (mapping.displayTextId == displayTextId);
        }
        mNavAdapter.notifyDataSetInvalidated();
    }
    private void handleNullNavigation(int displayTextId) {
        if(displayTextId == R.string.action_sign_out) {
            userManager.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
            setNavigationHighlighting(titleResourceId);
        }
        else {
            //remove the old fragment
            Fragment curr = fMgr.findFragmentById(R.id.frmPlaceholder);
            if(curr != null)
                fMgr.beginTransaction().remove(curr).commit();
            setNavigationHighlighting(-1);
        }
    }

    private List<FragmentNavigationMapping> createNavigationMappings() {
        ArrayList<FragmentNavigationMapping> nav = new ArrayList<>();
        final String currentLogin = userManager.getCachedCurrentUser().getLogin();
        nav.add(new FragmentNavigationMapping(R.string.title_fragment_currentUser, UserFragment.class) {
            @Override
            public Fragment createDefaultFragment() {
                return UserFragment.newInstance(currentLogin);
            }
        });
        nav.add(new FragmentNavigationMapping(R.string.action_sign_out, null));
        return nav;
    }

}
