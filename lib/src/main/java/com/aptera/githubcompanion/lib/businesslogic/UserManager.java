package com.aptera.githubcompanion.lib.businesslogic;

import com.aptera.githubcompanion.lib.data.IAuthHttpInterceptor;
import com.aptera.githubcompanion.lib.data.IGitHubApi;
import com.aptera.githubcompanion.lib.data.Response;
import com.aptera.githubcompanion.lib.model.User;

import retrofit.RetrofitError;

/**
 * Created by daschliman on 12/21/2015.
 */
public class UserManager implements IUserManager {

    private IGitHubApi mGitHubApi;
    private IAuthHttpInterceptor mAuthHttpInterceptor;
    private User mCurrentUser;

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public UserManager(IGitHubApi gitHubApi, IAuthHttpInterceptor authHttpInterceptor) {
        mGitHubApi = gitHubApi;
        mAuthHttpInterceptor = authHttpInterceptor;
        mCurrentUser = null;
    }

    public User Login(String username, String password) throws BusinessLogicException{
        try {
            Logout();
            mAuthHttpInterceptor.setCredentials(username, password);
            mCurrentUser = mGitHubApi.getUser(username);
            return mCurrentUser;
        }
        catch(Exception ex) {
            mAuthHttpInterceptor.setCredentials(null, null);
            if(ex instanceof RetrofitError && ((RetrofitError)ex).getResponse().getStatus() == 401)
                throw new BusinessLogicException("Invalid credentials.", ex);
            else
                ThrowGenericBusinessLogicError("logging in", ex);
        }
        return null;
    }

    public void Logout() {
        mAuthHttpInterceptor.setCredentials(null, null);
        mCurrentUser = null;
    }

    private void ThrowGenericBusinessLogicError(String action, Exception baseException) throws BusinessLogicException {
        throw new BusinessLogicException("An error occurred while " + action + ".", baseException);
    }

}
