package org.javaee8.jcache.infinispan.transaction.isolation;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.net.URI;


/**
 * @author Radim Hanus
 */
@ApplicationScoped
@Transactional
public class KeyValueService {
    private static final String CACHE_NAME = "defaultCache";

    private CacheManager cacheManager;
    private Cache<String, String> cache;

    @PostConstruct
    void init() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        URI configUri = URI.create("META-INF/infinispan.xml");
        cacheManager = cachingProvider.getCacheManager(configUri, cachingProvider.getDefaultClassLoader());
        cache = cacheManager.getCache(CACHE_NAME);
    }

    @PreDestroy
    void destroy() {
        cacheManager.close();
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public String get(String key) {
        return cache.get(key);
    }
}
