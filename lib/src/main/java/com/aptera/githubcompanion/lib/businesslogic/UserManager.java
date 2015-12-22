package com.aptera.githubcompanion.lib.businesslogic;

import com.aptera.githubcompanion.lib.data.IAuthHttpInterceptor;
import com.aptera.githubcompanion.lib.data.IGitHubApi;
import com.aptera.githubcompanion.lib.model.User;
import com.aptera.githubcompanion.lib.utilities.IStateful;
import com.aptera.githubcompanion.lib.utilities.IStatefulRegistry;

import java.io.Serializable;

import retrofit.RetrofitError;

/**
 * Created by daschliman on 12/21/2015.
 */
public class UserManager implements IUserManager, IStateful {

    private IGitHubApi mGitHubApi;
    private IAuthHttpInterceptor mAuthHttpInterceptor;
    private User mCurrentUser;

    public User getCachedCurrentUser() {
        return mCurrentUser;
    }

    public UserManager(IGitHubApi gitHubApi, IAuthHttpInterceptor authHttpInterceptor, IStatefulRegistry registry) {
        mGitHubApi = gitHubApi;
        mAuthHttpInterceptor = authHttpInterceptor;
        mCurrentUser = null;
        registry.registerOnly(this);
    }

    public User login(String username, String password) throws BusinessLogicException{
        try {
            logout();
            mAuthHttpInterceptor.setCredentials(username, password);
            mCurrentUser = loadCurrentUser();
            return mCurrentUser;
        }
        catch(Exception ex) {
            mAuthHttpInterceptor.setCredentials(null, null);
            throw ex;
        }
    }

    public void logout() {
        mAuthHttpInterceptor.setCredentials(null, null);
        mCurrentUser = null;
    }

    public User reloadCachedCurrentUser() throws BusinessLogicException {
        mCurrentUser = loadCurrentUser();
        return mCurrentUser;
    }

    @Override
    public Serializable getState() {
        return mCurrentUser;
    }

    @Override
    public void restoreState(Serializable state) {
        mCurrentUser = (User)state;
    }

    //support methods
    private User loadCurrentUser() throws BusinessLogicException {
        try {
            mCurrentUser = mGitHubApi.getAuthenticatedUser();
            return mCurrentUser;
        }
        catch(Exception ex) {
            if(ex instanceof RetrofitError && ((RetrofitError)ex).getResponse().getStatus() == 401)
                throw new BusinessLogicException("Invalid credentials.", ex);
            else
                throw BusinessLogicException.BuildWrapper("logging in", ex);
        }
    }

}
