package com.aptera.githubcompanion.lib.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by daschliman on 12/21/2015.
 */
public class StatefulRegistry implements IStatefulRegistry {

    private final Map<String, IStateful> mRegistry = new HashMap<>();

    public void register(String key, IStateful stateful) {
        mRegistry.put(key, stateful);
    }
    public void registerOnly(IStateful stateful) {
        mRegistry.put(stateful.getClass().getCanonicalName(), stateful);
    }
    public Set<Map.Entry<String, IStateful>> getEntries() {
        return mRegistry.entrySet();
    }
    public IStateful getEntry(String key) {
        return mRegistry.get(key);
    }

}
