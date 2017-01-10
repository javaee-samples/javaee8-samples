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


@RunWith(Arquillian.class)
public class CacheRemoveAllServiceTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CacheRemoveAllService.class)
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private CacheRemoveAllService service;

    @Test
    public void test() throws Exception {
        assertEquals("Dude_1", service.find("Dude"));
        assertEquals("Joe_2", service.find("Joe"));

        assertEquals("Dude_1", service.find("Dude"));
        assertEquals("Joe_2", service.find("Joe"));

        service.delete("Dude");
        assertEquals("Dude_3", service.find("Dude"));
        assertEquals("Joe_2", service.find("Joe"));

        service.deleteAll();
        assertEquals("Dude_4", service.find("Dude"));
        assertEquals("Joe_5", service.find("Joe"));
    }
}
