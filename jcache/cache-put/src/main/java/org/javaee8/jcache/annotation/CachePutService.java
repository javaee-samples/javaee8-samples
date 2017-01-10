package org.javaee8.jcache.annotation;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;


/**
 * @author Radim Hanus
 */
@CacheDefaults(cacheName = "cache.put.default")
public class CachePutService {
    @CachePut
    public void create(String id, @CacheValue String data) {
    }

    @CacheResult
    public String find(String id) {
        return null;
    }
}
