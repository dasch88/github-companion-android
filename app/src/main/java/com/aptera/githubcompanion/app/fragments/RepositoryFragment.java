package com.aptera.githubcompanion.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aptera.githubcompanion.R;
import com.aptera.githubcompanion.app.loaders.ResponseLoader;
import com.aptera.githubcompanion.lib.businesslogic.BusinessLogicException;
import com.aptera.githubcompanion.lib.businesslogic.IRepositoryManager;
import com.aptera.githubcompanion.lib.data.Response;
import com.aptera.githubcompanion.lib.model.Repository;

import javax.inject.Inject;

public class RepositoryFragment extends BaseFragment implements ITitled {
    public static final String ARG_OWNER_EXTRA = "com.aptera.githubcompanion.OWNER";
    public static final String ARG_REPOSITORY_NAME_EXTRA = "com.aptera.githubcompanion.REPOSITORY_NAME";
    public static final int REPOSITORY_LOADER_ID = 0;

    private String mOwner;
    private String mRepositoryName;

    private IFragmentNavigationListener mListener;

    // UI references.
    private TextView mTxtFullName;
    private TextView mTxtDescription;
    private TextView mTxtDefaultBranch;
    private TextView mTxtOpenIssueCount;
    private TextView mTxtSubscriberCount;

    @Inject
    public IRepositoryManager repositoryManager;

    public RepositoryFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String owner, String repoName) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OWNER_EXTRA, owner);
        args.putString(ARG_REPOSITORY_NAME_EXTRA, repoName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOwner = getArguments().getString(ARG_OWNER_EXTRA);
            mRepositoryName = getArguments().getString(ARG_REPOSITORY_NAME_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repository, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTxtFullName = (TextView) view.findViewById(R.id.txtFullName);
        mTxtDescription = (TextView) view.findViewById(R.id.txtDescription);
        mTxtDefaultBranch = (TextView) view.findViewById(R.id.txtDefaultBranch);
        mTxtOpenIssueCount = (TextView) view.findViewById(R.id.txtOpenIssueCount);
        mTxtSubscriberCount = (TextView) view.findViewById(R.id.txtSubscriberCount);

        //load user view with data
        loadRepository(getContext(), repositoryManager, mOwner, mRepositoryName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentNavigationListener) {
            mListener = (IFragmentNavigationListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IFragmentNavigationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void onRequestNavigation(Fragment frg) {
        if (mListener != null) {
            mListener.requestNavigation(frg);
        }
    }

    private void loadRepository(final Context ctx, final IRepositoryManager rMgr, final String ownerName, final String repoName) {
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
                    Toast.makeText(getContext(), data.getException().getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public int getTitleResourceId() {
        return R.string.title_fragment_repository;
    }
}
