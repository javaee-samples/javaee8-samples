package org.javaee8.jcache.configuration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class KeyValueServiceTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(KeyValueService.class, KeyValueEntryRemovedListener.class, KeyValueEntryEventFilter.class)
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private KeyValueService<String, String> service;

    @Test
    public void test() throws Exception {
        // empty both primary ans secondary caches
        assertNull(service.get("JSR107"));
        assertNull(service.getSecondary("JSR107"));

        assertNull(service.get("JSR367"));
        assertNull(service.getSecondary("JSR367"));

        // available only in primary cache
        service.put("JSR107", "JCACHE");
        assertEquals("JCACHE", service.get("JSR107"));
        assertNull(service.getSecondary("JSR107"));

        service.put("JSR367", "JSON-B");
        assertEquals("JSON-B", service.get("JSR367"));
        assertNull(service.getSecondary("JSR367"));

        // every second remove call is propagated as put operation on secondary cache
        service.remove("JSR107");
        assertNull(service.get("JSR107"));
        assertNull(service.getSecondary("JSR107"));

        // removed from primary cache, but available in secondary cache
        service.remove("JSR367");
        assertNull(service.get("JSR367"));
        assertEquals("JSON-B", service.getSecondary("JSR367"));
    }
}
