package org.javaee8.jcache.annotation;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;


/**
 * @author Radim Hanus
 */
@CacheDefaults(cacheName = "cache.default")
public class KeyValueService<K, V> {
    @CachePut
    public void put(K key, @CacheValue V value) {
    }

    @CacheResult
    public String get(K key) {
        return null;
    }
}
