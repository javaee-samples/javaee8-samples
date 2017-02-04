package org.javaee8.jcache.configuration;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.EventType;
import java.io.Serializable;


/**
 * Every second cache removed event is propagated further into cache listeners.
 *
 * @author Radim Hanus
 */
public class KeyValueEntryEventFilter<K, V> implements CacheEntryEventFilter<K, V>, Serializable {
    private static final long serialVersionUID = 1L;

    private int callCounter = 0;

    @Override
    public boolean evaluate(CacheEntryEvent<? extends K, ? extends V> event) throws CacheEntryListenerException {
        return event.getEventType().equals(EventType.REMOVED) && (++callCounter % 2 == 0);
    }
}
