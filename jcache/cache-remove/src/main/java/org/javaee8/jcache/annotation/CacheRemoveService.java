package org.javaee8.jcache.annotation;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;


/**
 * @author Radim Hanus
 */
@CacheDefaults(cacheName = "cache.remove.default")
public class CacheRemoveService {
    private int callCount = 0;

    @CacheResult
    public String find(String key) {
        callCount++;
        return key + "_" + callCount;
    }

    @CacheRemove
    public void delete(String key) {
    }
}
