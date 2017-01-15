package org.javaee8.jcache.configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.util.concurrent.TimeUnit;


/**
 * @author Radim Hanus
 */
public class KeyValueService<K,V> {
    static final long CACHE_TIMEOUT_MS = 100L;

    private CacheManager cacheManager;
    private Cache<K, V> cache;

    @PostConstruct
    void init() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();

        MutableConfiguration<K, V> config = new MutableConfiguration<>();

        // cache entries are supposed to expire if its last access time is older than 100 milliseconds
        config.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.MILLISECONDS, CACHE_TIMEOUT_MS)));

        cache = cacheManager.createCache("cache.default", config);
    }

    @PreDestroy
    void destroy() {
        cacheManager.close();
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }
}
