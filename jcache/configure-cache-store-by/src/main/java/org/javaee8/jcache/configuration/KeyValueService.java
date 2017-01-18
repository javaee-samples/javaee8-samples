package org.javaee8.jcache.configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;


/**
 * @author Radim Hanus
 */
public class KeyValueService<K,V> {
    public static final String CACHE_BY_VALUE = "cache.by.value";
    public static final String CACHE_BY_REFERENCE = "cache.by.reference";

    private CacheManager cacheManager;

    @PostConstruct
    void init() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();

        MutableConfiguration<K, V> config = new MutableConfiguration<>();
        // by default store by value both cache keys and values
        cacheManager.createCache(CACHE_BY_VALUE, config);

        // store both keys and values by reference so that their subsequent changes are reflected in cache
        config.setStoreByValue(false);
        cacheManager.createCache(CACHE_BY_REFERENCE, config);
    }

    @PreDestroy
    void destroy() {
        cacheManager.close();
    }

    public void put(String cacheName, K key, V value) {
        Cache<K, V> cache = cacheManager.getCache(cacheName);
        cache.put(key, value);
    }

    public V get(String cacheName, K key) {
        Cache<K, V> cache = cacheManager.getCache(cacheName);
        return cache.get(key);
    }
}
