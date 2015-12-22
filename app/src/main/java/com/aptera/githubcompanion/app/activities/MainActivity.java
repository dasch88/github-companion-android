package com.aptera.githubcompanion.app.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptera.githubcompanion.R;
import com.aptera.githubcompanion.app.BaseActivity;
import com.aptera.githubcompanion.app.adapters.DescribableArrayAdapter;
import com.aptera.githubcompanion.app.loaders.BitmapLoader;
import com.aptera.githubcompanion.app.loaders.ResponseLoader;
import com.aptera.githubcompanion.lib.businesslogic.BusinessLogicException;
import com.aptera.githubcompanion.lib.businesslogic.IRepositoryManager;
import com.aptera.githubcompanion.lib.businesslogic.IUserManager;
import com.aptera.githubcompanion.lib.data.IGitHubApi;
import com.aptera.githubcompanion.lib.data.Response;
import com.aptera.githubcompanion.lib.model.Repository;
import com.aptera.githubcompanion.lib.model.User;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private static final int PROFILE_IMAGE_LOADER_ID = 0;
    private static final int REPOSITORIES_LOADER_ID = 1;

    // UI references.
    private ImageView mImgProfileImage;
    private TextView mTxtUsername;
    private TextView mTxtName;
    private TextView mTxtEmail;
    private TextView mTxtFollowerCount;
    private TextView mTxtFollowingCount;
    private ListView mLstRepositories;

    @Inject
    public IUserManager userManager;

    @Inject
    public IRepositoryManager repositoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgProfileImage = (ImageView) findViewById(R.id.imgProfileImage);
        mTxtUsername = (TextView) findViewById(R.id.txtUsername);
        mTxtName = (TextView) findViewById(R.id.txtName);
        mTxtEmail = (TextView) findViewById(R.id.txtEmail);
        mTxtFollowerCount = (TextView) findViewById(R.id.txtFollowerCount);
        mTxtFollowingCount = (TextView) findViewById(R.id.txtFollowingCount);
        mLstRepositories = (ListView) findViewById(R.id.lstRepositories);

        //make sure that there is a currently logged in user. if not, load up the login activity
        if(userManager.getCachedCurrentUser() == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        else {
            setCurrentUserInfo();
        }
    }

    private void setCurrentUserInfo() {
        User currentUser = userManager.getCachedCurrentUser();
        mTxtUsername.setText(currentUser.getLogin());
        mTxtName.setText(currentUser.getName());
        mTxtEmail.setText(currentUser.getEmail());
        mTxtFollowerCount.setText(currentUser.getFollowers().toString());
        mTxtFollowingCount.setText(currentUser.getFollowing().toString());

        loadCurrentUserProfileImage(this, mImgProfileImage, currentUser.getAvatarUrl());
        loadCurrentUserRepositories(this, repositoryManager, mLstRepositories);
    }

    private void loadCurrentUserProfileImage(final Activity ctx, final ImageView imgView, final String profileImageUrl) {
        getLoaderManager().initLoader(PROFILE_IMAGE_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Bitmap>() {

            @Override
            public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
                return new BitmapLoader(ctx, profileImageUrl);
            }

            @Override
            public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
                imgView.setImageBitmap(data);
            }

            @Override
            public void onLoaderReset(Loader<Bitmap> loader) {

            }
        });
    }

    private void loadCurrentUserRepositories(final Activity ctx, final IRepositoryManager rMgr, final ListView lstRepositories) {
        getLoaderManager().initLoader(REPOSITORIES_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Response<Repository[]>>() {

            @Override
            public Loader<Response<Repository[]>> onCreateLoader(int id, Bundle args) {
                return new ResponseLoader<Repository[]>(ctx) {
                    @Override
                    protected Repository[] performLoad() throws BusinessLogicException {
                        return rMgr.getCurrentUserRepositories();
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Response<Repository[]>> loader, Response<Repository[]> data) {
                if (data.hasError()) {
                    Toast.makeText(getApplicationContext(), data.getException().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    final DescribableArrayAdapter<Repository> adapter = new DescribableArrayAdapter<Repository>(ctx, R.layout.listitem_describable,
                            R.id.txtName, R.id.txtDescription, data.getResult());
                    lstRepositories.setAdapter(adapter);
                }
            }

            @Override
            public void onLoaderReset(Loader<Response<Repository[]>> loader) {

            }
        });
    }
}
