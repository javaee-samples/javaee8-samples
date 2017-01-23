package org.javaee8.jcache.configuration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static junit.framework.TestCase.assertNotSame;
import static org.junit.Assert.*;


/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class KeyValueServiceTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(KeyValueService.class, MyValue.class, MyValueExternalizer.class)
                .addAsResource("infinispan.xml", "META-INF/infinispan.xml")
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private KeyValueService<String, MyValue> service;

    private String myKey = "JSR107";
    private MyValue myValue = new MyValue("JCACHE");

    @Test
    public void test() throws Exception {
        assertNull(service.get(myKey));
        service.put(myKey, this.myValue);

        MyValue myValue = service.get(myKey);

        // different references
        assertNotSame(this.myValue, myValue);
        // but the same values
        assertEquals(this.myValue, myValue);

        // change value returned from cache
        myValue.setValue("JSON-B");
        assertNotEquals(myValue, service.get(myKey));
    }
}
