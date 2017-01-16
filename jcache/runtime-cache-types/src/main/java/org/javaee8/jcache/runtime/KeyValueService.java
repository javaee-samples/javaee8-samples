package org.javaee8.jcache.runtime;

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
public class KeyValueService {
    private static final String CACHE_NAME = "cache.default";

    private CacheManager cacheManager;

    @PostConstruct
    void init() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();

        MutableConfiguration<String, String> config = new MutableConfiguration<>();

        // expected types of cache key and value
        config.setTypes(String.class, String.class);

        cacheManager.createCache(CACHE_NAME, config);
    }

    @PreDestroy
    void destroy() {
        cacheManager.close();
    }

    public void put(String key, String value) {
        // checks both key and value types are the same as configured
        Cache<String, String> cache = cacheManager.getCache(CACHE_NAME, String.class, String.class);
        cache.put(key, value);
    }

    public void put(Integer key, Integer value) {
        // key and value types mismatch, should throw a runtime exception
        Cache<Integer, Integer> cache = cacheManager.getCache(CACHE_NAME, Integer.class, Integer.class);
        cache.put(key, value);
    }
}
