package com.aptera.githubcompanion.lib.businesslogic;

import com.aptera.githubcompanion.lib.model.User;

/**
 * Created by daschliman on 12/21/2015.
 */
public interface IUserManager extends com.aptera.githubcompanion.lib.utilities.IStateful {
    User getCurrentUser();
    User Login(String username, String password) throws BusinessLogicException;
    void Logout();
}
