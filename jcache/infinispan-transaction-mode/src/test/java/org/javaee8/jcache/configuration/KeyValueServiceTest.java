package org.javaee8.jcache.configuration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

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
                .addClasses(KeyValueService.class)
                .addAsResource("infinispan.xml", "META-INF/infinispan.xml")
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private KeyValueService<String, String> service;
    @Inject
    private UserTransaction transaction;

    @Test
    public void test() throws Exception {
        transaction.begin();
        service.put("JSR107", "JCACHE");
        service.put("JSR365", "CDI 2.0");
        transaction.commit();

        assertEquals("JCACHE", service.get("JSR107"));
        assertEquals("CDI 2.0", service.get("JSR365"));

        transaction.begin();
        service.put("JSR367", "JSON-B");
        service.put("JSR371", "MVC 1.0");
        transaction.rollback();

        assertNull(service.get("JSR367"));
        assertNull(service.get("JSR371"));
    }
}
