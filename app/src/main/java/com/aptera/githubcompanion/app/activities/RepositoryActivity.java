package com.aptera.githubcompanion.app.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.aptera.githubcompanion.R;
import com.aptera.githubcompanion.app.BaseActivity;
import com.aptera.githubcompanion.app.loaders.ResponseLoader;
import com.aptera.githubcompanion.lib.businesslogic.BusinessLogicException;
import com.aptera.githubcompanion.lib.businesslogic.IRepositoryManager;
import com.aptera.githubcompanion.lib.businesslogic.IUserManager;
import com.aptera.githubcompanion.lib.data.Response;
import com.aptera.githubcompanion.lib.model.Repository;

import javax.inject.Inject;

public class RepositoryActivity extends BaseActivity {

    public static final String OWNER_EXTRA = "com.aptera.githubcompanion.OWNER_EXTRA";
    public static final String REPOSITORY_NAME_EXTRA = "com.aptera.githubcompanion.REPOSITORY_NAME_EXTRA";
    public static final int REPOSITORY_LOADER_ID = 0;

    // UI references.
    private TextView mTxtFullName;
    private TextView mTxtDescription;
    private TextView mTxtDefaultBranch;
    private TextView mTxtOpenIssueCount;
    private TextView mTxtSubscriberCount;

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
            setContentView(R.layout.activity_repository);

            mTxtFullName = (TextView) findViewById(R.id.txtFullName);
            mTxtDescription = (TextView) findViewById(R.id.txtDescription);
            mTxtDefaultBranch = (TextView) findViewById(R.id.txtDefaultBranch);
            mTxtOpenIssueCount = (TextView) findViewById(R.id.txtOpenIssueCount);
            mTxtSubscriberCount = (TextView) findViewById(R.id.txtSubscriberCount);

            String ownerName = getIntent().getStringExtra(OWNER_EXTRA);
            String repoName = getIntent().getStringExtra(REPOSITORY_NAME_EXTRA);
            loadRepository(this, repositoryManager, ownerName, repoName);
        }


    }

    private void loadRepository(final Activity ctx, final IRepositoryManager rMgr, final String ownerName, final String repoName) {
        getLoaderManager().initLoader(REPOSITORY_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Response<Repository>>() {

            @Override
            public Loader<Response<Repository>> onCreateLoader(int id, Bundle args) {
                return new ResponseLoader<Repository>(ctx) {
                    @Override
                    protected Repository performLoad() throws BusinessLogicException {
                        return rMgr.getRepository(ownerName, repoName);
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Response<Repository>> loader, Response<Repository> data) {
                if (data.hasError()) {
                    Toast.makeText(getApplicationContext(), data.getException().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    setRepositoryInfo(data.getResult());
                }
            }

            @Override
            public void onLoaderReset(Loader<Response<Repository>> loader) {

            }
        });
    }

    private void setRepositoryInfo(Repository repo) {
        mTxtFullName.setText(repo.getFullName());
        mTxtDescription.setText(repo.getDescription());
        mTxtDefaultBranch.setText(repo.getDefaultBranch());
        mTxtOpenIssueCount.setText(repo.getOpenIssuesCount().toString());
        mTxtSubscriberCount.setText(repo.getSubscribersCount().toString());
    }
}
