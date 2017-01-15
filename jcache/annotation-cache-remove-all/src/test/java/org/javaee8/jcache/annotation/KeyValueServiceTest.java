package org.javaee8.jcache.annotation;

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


@RunWith(Arquillian.class)
public class KeyValueServiceTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(KeyValueService.class)
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private KeyValueService<String, String> service;

    @Test
    public void test() throws Exception {
        assertNull(service.get("JSR107"));
        assertNull(service.get("JSR367"));

        service.put("JSR107", "JCACHE");
        assertEquals("JCACHE", service.get("JSR107"));

        service.put("JSR367", "JSON-B");
        assertEquals("JSON-B", service.get("JSR367"));

        service.deleteAll();
        assertNull(service.get("JSR107"));
        assertNull(service.get("JSR367"));
    }
}
