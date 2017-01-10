package org.javaee8.jcache.annotation;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheResult;


/**
 * @author Radim Hanus
 */
@CacheDefaults(cacheName = "org.javaee8.jcache.annotation.DefaultCache")
public class NamedCacheService {
    private int nameCallCount = 0;
    private int dataCallCount = 0;

    @CacheResult
    public String getName(String name) {
        nameCallCount++;
        return name + "_" + nameCallCount;
    }

    @CacheResult(cacheName = "org.javaee8.jcache.annotation.DataCache")
    public String getData(String name) {
        dataCallCount++;
        return name + "_data_" + dataCallCount;
    }
}
