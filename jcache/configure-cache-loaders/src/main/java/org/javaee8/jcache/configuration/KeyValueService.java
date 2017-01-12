package org.javaee8.jcache.configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author Radim Hanus
 */
public class KeyValueService {
    private CacheManager cacheManager;
    private Cache<String, String> cache;

    @PostConstruct
    void init() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();
        MutableConfiguration<String, String> config = new MutableConfiguration<>();

        // synchronization with an external resource when cache entries are read
        Factory<KeyValueLoaderWriter> factory = FactoryBuilder.factoryOf(new KeyValueLoaderWriter());
        config.setCacheLoaderFactory(factory).setReadThrough(true);
        // synchronization with an external resource when cache entries are updated and deleted
        config.setCacheWriterFactory(factory).setWriteThrough(true);

        // type checking on cache operations
        config.setTypes(String.class, String.class);
        cache = cacheManager.createCache("cache.default", config);
    }

    @PreDestroy
    void destroy() {
        cacheManager.close();
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public void putAll(Map<String, String> map) {
        cache.putAll(map);
    }

    public String get(String key) {
        return cache.get(key);
    }

    public Map<String, String> getAll(String... keys) {
        return cache.getAll(Arrays.stream(keys).collect(Collectors.toSet()));
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void removeAll(String... keys) {
        cache.removeAll(Arrays.stream(keys).collect(Collectors.toSet()));
    }
}
