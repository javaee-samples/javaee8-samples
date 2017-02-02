package org.javaee8.jcache.infinispan.transaction.isolation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;
import java.util.concurrent.CyclicBarrier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class ReadCommittedIsolationTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(KeyValueService.class)
                .addAsResource("infinispan-isolation-read_commited.xml", "META-INF/infinispan.xml")
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Resource
    private ManagedExecutorService executorService;

    @Inject
    private KeyValueService service;

    @Test
    public void test() throws Exception {
        // put an entry
        service.put("JSR107", "JCACHE");

        final CyclicBarrier barrier = new CyclicBarrier(2);

        executorService.execute(() -> {
            try {
                UserTransaction transaction = InitialContext.doLookup("java:comp/UserTransaction");
                transaction.begin();

                // ensure both threads started a tx and see expected value
                assertEquals("JCACHE", service.get("JSR107"));
                barrier.await();

                // update the entry value
                service.put("JSR107", "CDI 2.0");

                transaction.commit();

                // signal the change
                barrier.await();
            } catch (Throwable e) {
                fail(e.getMessage());
            }
        });

        executorService.execute(() -> {
            try {
                UserTransaction transaction = InitialContext.doLookup("java:comp/UserTransaction");
                transaction.begin();

                // ensure both threads started a tx and see expected value
                assertEquals("JCACHE", service.get("JSR107"));
                barrier.await();

                // entry value has been changed
                barrier.await();

                // should see the change in existing tx
                assertEquals("CDI 2.0", service.get("JSR107"));

                transaction.commit();
            } catch (Throwable e) {
                fail(e.getMessage());
            }
        });
    }
}
