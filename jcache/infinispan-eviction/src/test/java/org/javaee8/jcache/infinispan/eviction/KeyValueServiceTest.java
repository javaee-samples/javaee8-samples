package org.javaee8.jcache.infinispan.eviction;

import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * @author Radim Hanus
 */
abstract class KeyValueServiceTest {
    @Inject
    private KeyValueService<String, String> service;

    @Test
    public void test() throws Exception {
        assertNull(service.get("JSR107"));
        service.put("JSR107", "JCACHE");
        assertEquals("JCACHE", service.get("JSR107"));
        assertEquals(1, service.getKeys().size());

        assertNull(service.get("JSR365"));
        service.put("JSR365", "CDI 2.0");
        assertEquals("CDI 2.0", service.get("JSR365"));
        assertEquals(2, service.getKeys().size());

        assertNull(service.get("JSR367"));
        service.put("JSR367", "JSON-B");
        assertEquals("JSON-B", service.get("JSR367"));
        assertEquals(2, service.getKeys().size());

        assertNull(service.get("JSR371"));
        service.put("JSR371", "MVC 1.0");
        assertEquals("MVC 1.0", service.get("JSR371"));
        assertEquals(2, service.getKeys().size());
    }
}
