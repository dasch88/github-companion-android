package com.aptera.githubcompanion.app.viewmodels;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * Created by daschliman on 12/29/2015.
 */
public class FragmentNavigationMapping implements Serializable {
    public int displayTextId;
    public Class<? extends Fragment> fragmentClass;
    public boolean isSelected;
    public FragmentNavigationMapping(int displayTextId, Class<? extends Fragment> fragmentClass) {
        this.displayTextId = displayTextId;
        this.fragmentClass = fragmentClass;
        this.isSelected = false;
    }

    public Fragment createDefaultFragment() {
        return null;
    }
}
