package com.aptera.githubcompanion.lib.utilities;

import java.util.Map;
import java.util.Set;

/**
 * Created by daschliman on 12/21/2015.
 */
public interface IStatefulRegistry {
    void register(String key, IStateful stateful);
    void registerOnly(IStateful stateful);
    Set<Map.Entry<String, IStateful>> getEntries();
    IStateful getEntry(String key);
}
