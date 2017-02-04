package org.javaee8.jcache.configuration;

import javax.cache.Cache;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryRemovedListener;
import java.io.Serializable;


/**
 * @author Radim Hanus
 */
public class KeyValueEntryRemovedListener<K, V> implements CacheEntryRemovedListener<K, V>, Serializable {
    private static final long serialVersionUID = 1L;

    private Cache<K, V> removedEntriesCache;

    public KeyValueEntryRemovedListener(Cache<K, V> removedEntriesCache) {
        this.removedEntriesCache = removedEntriesCache;
    }

    @Override
    public void onRemoved(Iterable<CacheEntryEvent<? extends K, ? extends V>> cacheEntryEvents) throws CacheEntryListenerException {
        cacheEntryEvents.forEach((entry) -> removedEntriesCache.put(entry.getKey(), entry.getValue()));
    }
}
