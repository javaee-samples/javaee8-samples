package org.javaee8.jcache.infinispan.eviction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;


/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class OffHeapStorageTest extends KeyValueServiceTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(KeyValueService.class, KeyValueServiceTest.class)
                .addAsResource("infinispan-off-heap.xml", "META-INF/infinispan.xml")
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

}
