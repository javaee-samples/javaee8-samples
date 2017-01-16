package org.javaee8.jcache.runtime;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


/**
 * @author Radim Hanus
 */
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
    private KeyValueService service;

    @Test
    public void test() throws Exception {
        // available in cache
        service.put("JSR107", "JCACHE");
    }

    @Test
    public void testIllegalTypes() throws Exception {
        try {
            service.put(1, 10);
            fail();
        } catch (Exception e) {
            assertThat(e, either(instanceOf(IllegalArgumentException.class)).or(instanceOf(ClassCastException.class)));
        }
    }
}
