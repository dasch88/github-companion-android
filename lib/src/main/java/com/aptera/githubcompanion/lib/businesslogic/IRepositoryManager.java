package com.aptera.githubcompanion.lib.businesslogic;

import com.aptera.githubcompanion.lib.model.Repository;
import com.aptera.githubcompanion.lib.model.User;

/**
 * Created by daschliman on 12/21/2015.
 */
public interface IRepositoryManager {
    Repository[] getCurrentUserRepositories() throws BusinessLogicException;
}
