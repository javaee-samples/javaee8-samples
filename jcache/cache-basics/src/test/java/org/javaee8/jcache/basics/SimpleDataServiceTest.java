package org.javaee8.jcache.basics;

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
public class SimpleDataServiceTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(SimpleDataService.class)
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private SimpleDataService service;

    @Test
    public void test() throws Exception {
        assertNull(service.get("Dude"));

        service.put("Dude", "White Russian");
        assertEquals("White Russian", service.get("Dude"));

        service.remove("Dude");
        assertNull(service.get("Dude"));
    }
}
