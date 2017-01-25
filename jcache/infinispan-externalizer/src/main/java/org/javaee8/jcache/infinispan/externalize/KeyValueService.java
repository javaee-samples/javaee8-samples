package org.javaee8.jcache.infinispan.externalize;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.net.URI;


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
}
