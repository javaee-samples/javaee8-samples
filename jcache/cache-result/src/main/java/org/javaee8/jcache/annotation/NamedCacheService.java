package org.javaee8.jcache.annotation;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheResult;


/**
 * @author Radim Hanus
 */
@CacheDefaults(cacheName = "cache.default")
public class NamedCacheService {
    private int nameCallCount = 0;
    private int dataCallCount = 0;

    @CacheResult
    public String getName(String name) {
        nameCallCount++;
        return name + "_" + nameCallCount;
    }

    @CacheResult(cacheName = "cache.data")
    public String getData(String name) {
        dataCallCount++;
        return name + "_data_" + dataCallCount;
    }
}
