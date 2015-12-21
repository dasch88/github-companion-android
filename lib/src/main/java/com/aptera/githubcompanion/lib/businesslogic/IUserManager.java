package com.aptera.githubcompanion.lib.businesslogic;

import com.aptera.githubcompanion.lib.data.Response;
import com.aptera.githubcompanion.lib.model.User;

/**
 * Created by daschliman on 12/21/2015.
 */
public interface IUserManager {
    User Login(String username, String password) throws BusinessLogicException;
}
