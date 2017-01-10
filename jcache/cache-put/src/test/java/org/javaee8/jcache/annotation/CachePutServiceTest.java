package org.javaee8.jcache.annotation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;


@RunWith(Arquillian.class)
public class CachePutServiceTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CachePutService.class)
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private CachePutService service;

    @Test
    public void test() throws Exception {
        service.create("ticket_1", "JCACHE");

        assertNotNull(service.find("ticket_1"));
        assertEquals("JCACHE", service.find("ticket_1"));

        assertNull(service.find("ticket_2"));
    }
}
