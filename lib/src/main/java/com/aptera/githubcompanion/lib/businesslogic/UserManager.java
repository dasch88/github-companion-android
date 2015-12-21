package com.aptera.githubcompanion.lib.businesslogic;

import com.aptera.githubcompanion.lib.data.IAuthHttpInterceptor;
import com.aptera.githubcompanion.lib.data.IGitHubApi;
import com.aptera.githubcompanion.lib.data.Response;
import com.aptera.githubcompanion.lib.model.User;

/**
 * Created by daschliman on 12/21/2015.
 */
public class UserManager implements IUserManager {

    private IGitHubApi mGitHubApi;
    private IAuthHttpInterceptor mAuthHttpInterceptor;

    public UserManager(IGitHubApi gitHubApi, IAuthHttpInterceptor authHttpInterceptor) {
        mGitHubApi = gitHubApi;
        mAuthHttpInterceptor = authHttpInterceptor;
    }

    public User Login(String username, String password) throws BusinessLogicException{
        try {
            mAuthHttpInterceptor.setCredentials(username, password);
            User result = mGitHubApi.getUser(username);
            return result;
        }
        catch(Exception ex) {
            throw new BusinessLogicException("An error occurred logging in.", ex);
        }
    }


}
