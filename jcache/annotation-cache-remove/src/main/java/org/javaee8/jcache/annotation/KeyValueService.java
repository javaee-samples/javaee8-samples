package org.javaee8.jcache.annotation;

import javax.cache.annotation.*;


/**
 * @author Radim Hanus
 */
@CacheDefaults(cacheName = "cache.default")
public class KeyValueService<K, V> {
    @CachePut
    public void put(K key, @CacheValue V value) {
    }

    @CacheResult
    public V get(K key) {
        return null;
    }

    @CacheRemove
    public void delete(K key) {
    }
}
