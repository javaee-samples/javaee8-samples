package org.javaee8.cdi.dynamic.bean.decorated;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class DynamicBeanTest {

    @Deployment
    public static WebArchive deploy() {
        return create(WebArchive.class)
                .addAsLibraries(
                    create(JavaArchive.class)
                        .addClasses(CdiExtension.class, MyBean.class, MyBeanImpl.class)
                        .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension"))
                .addClass(MyDecorator.class)
                .addAsManifestResource("beans.xml");
    }

    @Inject
    private MyBean myBean;

    @Test
    public void test() {
        assertEquals("Hi! decorated", myBean.sayHi());
    }
}
