package org.javaee8.jcache.configuration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;


/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class KeyValueServiceTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(KeyValueService.class, MyKey.class, MyValue.class)
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private KeyValueService<MyKey, MyValue> service;

    private MyKey myKey = new MyKey("JSR107");
    private MyValue myValue = new MyValue("JCACHE");

    // todo: store by value produces CNFE
/*
    @Test
    public void testImmutable() throws Exception {
        // use cache where keys and values are maintained by value
        String cacheName = KeyValueService.CACHE_BY_VALUE;

        service.put(cacheName, myKey, this.myValue);
        MyValue myValue = service.get(cacheName, myKey);

        // different references
        assertNotSame(this.myValue, myValue);
        // but the same values
        assertEquals(this.myValue, myValue);

        // change value returned from cache
        myValue.setValue("JSON-B");
        assertNotEquals(myValue, service.get(cacheName, myKey));
    }
*/

    @Test
    public void testMutable() throws Exception {
        // use cache where keys and values are maintained by reference
        String cacheName = KeyValueService.CACHE_BY_REFERENCE;

        service.put(cacheName, myKey, this.myValue);
        MyValue myValue = service.get(cacheName, myKey);

        // the same references (and thus the same values)
        assertSame(this.myValue, myValue);
    }
}
