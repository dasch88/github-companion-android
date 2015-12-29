package com.aptera.githubcompanion.app.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptera.githubcompanion.R;
import com.aptera.githubcompanion.app.adapters.DescribableRecyclerAdapter;
import com.aptera.githubcompanion.app.loaders.BitmapLoader;
import com.aptera.githubcompanion.app.loaders.ResponseLoader;
import com.aptera.githubcompanion.lib.businesslogic.BusinessLogicException;
import com.aptera.githubcompanion.lib.businesslogic.IRepositoryManager;
import com.aptera.githubcompanion.lib.businesslogic.IUserManager;
import com.aptera.githubcompanion.lib.data.Response;
import com.aptera.githubcompanion.lib.model.Repository;
import com.aptera.githubcompanion.lib.model.User;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class UserFragment extends BaseFragment implements ITitled {

    private static final String ARG_USER_LOGIN = "com.aptera.githubcompanion.USER_LOGIN";
    private static final int PROFILE_IMAGE_LOADER_ID = 0;
    private static final int USER_LOADER_ID = 1;
    private static final int REPOSITORIES_LOADER_ID = 2;

    private String mUserLogin;

    private IFragmentNavigationListener mListener;
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

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String userLogin) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_LOGIN, userLogin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserLogin = getUserLoginFromArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImgProfileImage = (ImageView) view.findViewById(R.id.imgProfileImage);
        mTxtUsername = (TextView) view.findViewById(R.id.txtUsername);
        mTxtName = (TextView) view.findViewById(R.id.txtName);
        mTxtEmail = (TextView) view.findViewById(R.id.txtEmail);
        mTxtFollowerCount = (TextView) view.findViewById(R.id.txtFollowerCount);
        mTxtFollowingCount = (TextView) view.findViewById(R.id.txtFollowingCount);
        mLstRepositories = (RecyclerView) view.findViewById(R.id.lstRepositories);

        //setup repositories
        mRepositoriesAdapter =
                new DescribableRecyclerAdapter<Repository>(R.layout.listitem_describable, R.id.txtName, R.id.txtDescription) {
                    @Override
                    public void onClick(View v) {
                        onRepositoryViewClicked(v);
                    }
                };
        mLstRepositories.setLayoutManager(new LinearLayoutManager(getContext()));
        mLstRepositories.setAdapter(mRepositoriesAdapter);

        //load user view with data
        if(mUserLogin.equals(userManager.getCachedCurrentUser().getLogin())) {
            setUserInfo(userManager.getCachedCurrentUser());
        }
        else {
            loadUser(getContext(), mUserLogin, userManager);
        }
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

    private void setUserInfo(User user) {
        mTxtUsername.setText(user.getLogin());
        mTxtName.setText(user.getName());
        mTxtEmail.setText(user.getEmail());
        mTxtFollowerCount.setText(user.getFollowers().toString());
        mTxtFollowingCount.setText(user.getFollowing().toString());

        loadUserProfileImage(getContext(), mImgProfileImage, user.getAvatarUrl());
        loadUserRepositories(getContext(), user.getLogin(), repositoryManager);
    }



    private void loadUserProfileImage(final Context ctx, final ImageView imgView, final String profileImageUrl) {
        getLoaderManager().initLoader(PROFILE_IMAGE_LOADER_ID, null, BitmapLoader.createImageLoaderCallbacks(getContext(), imgView, profileImageUrl));
    }

    private void loadUser(final Context ctx, final String userLogin, final IUserManager uMgr) {
        getLoaderManager().initLoader(USER_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Response<User>>() {

            @Override
            public Loader<Response<User>> onCreateLoader(int id, Bundle args) {
                return new ResponseLoader<User>(ctx) {
                    @Override
                    protected User performLoad() throws BusinessLogicException {
                        return uMgr.getUser(userLogin);
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Response<User>> loader, Response<User> data) {
                if (data.hasError()) {
                    Toast.makeText(getContext(), data.getException().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    setUserInfo(data.getResult());
                }
            }

            @Override
            public void onLoaderReset(Loader<Response<User>> loader) {

            }
        });
    }

    private void loadUserRepositories(final Context ctx, final String ownerLogin, final IRepositoryManager rMgr) {
        getLoaderManager().initLoader(REPOSITORIES_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Response<Repository[]>>() {

            @Override
            public Loader<Response<Repository[]>> onCreateLoader(int id, Bundle args) {
                return new ResponseLoader<Repository[]>(ctx) {
                    @Override
                    protected Repository[] performLoad() throws BusinessLogicException {
                        return rMgr.getUserRepositories(ownerLogin);
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Response<Repository[]>> loader, Response<Repository[]> data) {
                if (data.hasError()) {
                    Toast.makeText(getContext(), data.getException().getMessage(), Toast.LENGTH_LONG).show();
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
        Fragment frg = RepositoryFragment.newInstance(repo.getOwner().getLogin(), repo.getName());
        onRequestNavigation(frg);
    }

    private String getUserLoginFromArguments() {
        if(getArguments() != null)
            return getArguments().getString(ARG_USER_LOGIN);
        else
            return null;
    }

    @Override
    public int getTitleResourceId() {
        //double check the user login is loaded, since this can be called before it's ready
        String user = (mUserLogin != null ? mUserLogin : getUserLoginFromArguments());
        if (user != null && user.equals(userManager.getCachedCurrentUser().getLogin()))
            return R.string.title_fragment_currentUser;
        else
            return R.string.title_fragment_user;
    }
}
