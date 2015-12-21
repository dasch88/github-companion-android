package com.aptera.githubcompanion.lib.utilities;

import java.io.Serializable;

/**
 * Created by daschliman on 12/21/2015.
 */
public interface IStateful {
    Serializable getState();
    void restoreState(Serializable state);
}
