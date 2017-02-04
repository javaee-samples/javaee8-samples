package org.javaee8.jcache.infinispan.eviction;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Radim Hanus
 */
public class KeyValueService<K,V> {
    public static final String CACHE_NAME = "defaultCache";

    private CacheManager cacheManager;

    @PostConstruct
    void init() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        URI configUri = URI.create("META-INF/infinispan.xml");
        cacheManager = cachingProvider.getCacheManager(configUri, cachingProvider.getDefaultClassLoader());
    }

    @PreDestroy
    void destroy() {
        cacheManager.close();
    }

    public void put(K key, V value) {
        Cache<K, V> cache = cacheManager.getCache(CACHE_NAME);
        cache.put(key, value);
    }

    public V get(K key) {
        Cache<K, V> cache = cacheManager.getCache(CACHE_NAME);
        return cache.get(key);
    }

    public Set<K> getKeys() {
        Cache<K, V> cache = cacheManager.getCache(CACHE_NAME);
        Set<K> keys = new HashSet<>();
        for (Cache.Entry<K, V> entry : cache) {
            keys.add(entry.getKey());
        }
        return keys;
    }
}
