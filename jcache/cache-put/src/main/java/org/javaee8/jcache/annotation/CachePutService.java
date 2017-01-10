package org.javaee8.jcache.annotation;

import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;


/**
 * @author Radim Hanus
 */
public class CachePutService {
    @CachePut(cacheName = "cache.put.sample")
    public void create(String id, @CacheValue String data) {
    }

    @CacheResult(cacheName = "cache.put.sample")
    public String find(String id) {
        return null;
    }
}
