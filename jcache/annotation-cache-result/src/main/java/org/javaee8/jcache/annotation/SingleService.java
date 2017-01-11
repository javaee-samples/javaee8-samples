package org.javaee8.jcache.annotation;

import javax.cache.annotation.CacheResult;


/**
 * @author Radim Hanus
 */
public class SingleService {
    private int callCount = 0;

    @CacheResult
    public String getName(String name) {
        callCount++;
        return name + "_" + callCount;
    }
}
