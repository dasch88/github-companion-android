package com.aptera.githubcompanion.app.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptera.githubcompanion.R;
import com.aptera.githubcompanion.app.BaseActivity;
import com.aptera.githubcompanion.app.adapters.DescribableRecyclerAdapter;
import com.aptera.githubcompanion.app.loaders.BitmapLoader;
import com.aptera.githubcompanion.app.loaders.ResponseLoader;
import com.aptera.githubcompanion.lib.businesslogic.BusinessLogicException;
import com.aptera.githubcompanion.lib.businesslogic.IRepositoryManager;
import com.aptera.githubcompanion.lib.businesslogic.IUserManager;
import com.aptera.githubcompanion.lib.data.Response;
import com.aptera.githubcompanion.lib.model.Repository;
import com.aptera.githubcompanion.lib.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private static final int PROFILE_IMAGE_LOADER_ID = 0;
    private static final int REPOSITORIES_LOADER_ID = 1;

    private DescribableRecyclerAdapter<Repository> mRepositoriesAdapter;

    // UI references.
    private ImageView mImgProfileImage;
    private TextView mTxtUsername;
    private TextView mTxtName;
    private TextView mTxtEmail;
    private TextView mTxtFollowerCount;
    private TextView mTxtFollowingCount;
    private RecyclerView mLstRepositories;

    @Inject
    public IUserManager userManager;

    @Inject
    public IRepositoryManager repositoryManager;

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

            mImgProfileImage = (ImageView) findViewById(R.id.imgProfileImage);
            mTxtUsername = (TextView) findViewById(R.id.txtUsername);
            mTxtName = (TextView) findViewById(R.id.txtName);
            mTxtEmail = (TextView) findViewById(R.id.txtEmail);
            mTxtFollowerCount = (TextView) findViewById(R.id.txtFollowerCount);
            mTxtFollowingCount = (TextView) findViewById(R.id.txtFollowingCount);
            mLstRepositories = (RecyclerView) findViewById(R.id.lstRepositories);


            mRepositoriesAdapter =
            new DescribableRecyclerAdapter<Repository>(R.layout.listitem_describable, R.id.txtName, R.id.txtDescription) {
                @Override
                public void onClick(View v) {
                    onRepositoryViewClicked(v);
                }
            };

            mLstRepositories.setLayoutManager(new LinearLayoutManager(this));
            mLstRepositories.setAdapter(mRepositoriesAdapter);

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
        loadCurrentUserRepositories(this, repositoryManager);
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

    private void loadCurrentUserRepositories(final Activity ctx, final IRepositoryManager rMgr) {
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
                    final List<Repository> dataList = Arrays.asList(data.getResult());
                    mRepositoriesAdapter.setDataset(dataList);
                }
            }

            @Override
            public void onLoaderReset(Loader<Response<Repository[]>> loader) {

            }
        });
    }

    private void onRepositoryViewClicked(View v) {
        Repository repo = mRepositoriesAdapter.getItem(mLstRepositories.getChildAdapterPosition(v));
        Intent repoIntent = new Intent(this, RepositoryActivity.class);
        startActivity(repoIntent);
    }
}
