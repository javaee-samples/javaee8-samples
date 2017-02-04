package org.javaee8.jcache.configuration;

import javax.cache.Cache;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriter;
import javax.cache.integration.CacheWriterException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Radim Hanus
 */
public class KeyValueLoaderWriter implements CacheLoader<String, String>, CacheWriter<String, String>, Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, String> storage = new HashMap<>();

    KeyValueLoaderWriter() {
        storage.put("JSR107", "JCACHE");
        storage.put("JSR365", "CDI 2.0");
        storage.put("JSR367", "JSON-B");
    }

    @Override
    public String load(String key) throws CacheLoaderException {
        return storage.get(key);
    }

    @Override
    public Map<String, String> loadAll(Iterable<? extends String> keys) throws CacheLoaderException {
        final Map<String, String> result = new HashMap<>();
        keys.forEach((key) -> {
            String value = load(key);
            if (value != null) {
                result.put(key, value);
            }
        });
        return result;
    }

    @Override
    public void write(Cache.Entry<? extends String, ? extends String> entry) throws CacheWriterException {
        storage.put(entry.getKey(), entry.getValue());
    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends String, ? extends String>> entries) throws CacheWriterException {
        entries.forEach(this::write);
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        if (key instanceof String) {
            storage.remove(key);
        }
    }

    @Override
    public void deleteAll(Collection<?> keys) throws CacheWriterException {
        keys.forEach(this::delete);
    }
}
