package com.aptera.githubcompanion.lib.businesslogic;

import com.aptera.githubcompanion.lib.data.IGitHubApi;
import com.aptera.githubcompanion.lib.model.Repository;

import retrofit.RetrofitError;

/**
 * Created by daschliman on 12/22/2015.
 */
public class RepositoryManager implements IRepositoryManager {

    private IGitHubApi mGitHubApi;

    public RepositoryManager(IGitHubApi gitHubApi) {
        mGitHubApi = gitHubApi;
    }

    @Override
    public Repository[] getCurrentUserRepositories() throws BusinessLogicException {
        try {
            return mGitHubApi.getAuthenticatedUserRepositories();
        }
        catch(Exception ex) {
            throw BusinessLogicException.BuildWrapper("loading current user repositories", ex);
        }
    }
}
