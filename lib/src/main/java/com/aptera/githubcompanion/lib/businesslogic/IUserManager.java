package com.aptera.githubcompanion.lib.businesslogic;

import com.aptera.githubcompanion.lib.model.User;

/**
 * Created by daschliman on 12/21/2015.
 */
public interface IUserManager extends com.aptera.githubcompanion.lib.utilities.IStateful {
    User getCachedCurrentUser();
    User login(String username, String password) throws BusinessLogicException;
    void logout();
    User reloadCachedCurrentUser() throws BusinessLogicException;
}
