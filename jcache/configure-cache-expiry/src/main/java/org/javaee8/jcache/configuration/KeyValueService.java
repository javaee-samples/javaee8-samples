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
    private Cache<K, V> cache;

    @PostConstruct
    void init() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        MutableConfiguration<K, V> config = new MutableConfiguration<>();

        // cache entries are supposed to expire if its last access time is older than 1 sec
        config.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 1)));

        cache = cacheManager.createCache("cache.default", config);
    }

    @PreDestroy
    void destroy() {
        cache.close();
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void remove(K key) {
        cache.remove(key);
    }
}
