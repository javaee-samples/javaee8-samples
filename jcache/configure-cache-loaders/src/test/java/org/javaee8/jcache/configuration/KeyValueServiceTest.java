package org.javaee8.jcache.configuration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

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
                .addClasses(KeyValueService.class, KeyValueLoaderWriter.class)
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private KeyValueService service;

    @Test
    public void test() throws Exception {
        // should be available because of cache loader
        assertEquals("JCACHE", service.get("JSR107"));
        assertEquals("CDI 2.0", service.get("JSR365"));
        assertEquals("JSON-B", service.get("JSR367"));

        // remove single entry
        service.remove("JSR367");
        assertNull(service.get("JSR367"));

        // add single entry
        assertNull(service.get("JSR371"));
        service.put("JSR371", "MVC 1.0");
        assertEquals("MVC 1.0", service.get("JSR371"));

        // remove multiple entries
        service.removeAll("JSR107", "JSR365");
        assertNull(service.get("JSR107"));
        assertNull(service.get("JSR365"));

        // add multiple entries
        Map<String, String> newSpecs = new HashMap<>();
        newSpecs.put("JSR370", "JAX-RS 2.1");
        newSpecs.put("JSR372", "JSF 2.3");
        service.putAll(newSpecs);
        assertEquals("JAX-RS 2.1", service.get("JSR370"));
        assertEquals("JSF 2.3", service.get("JSR372"));
    }
}
