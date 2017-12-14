package org.javaee8.cdi.bean.discovery.none;

import static org.junit.Assert.assertTrue;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.javaee8.cdi.bean.discovery.disabled.CdiDisabledBean;
import org.javaee8.cdi.bean.discovery.enabled.CdiEnabledBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CdiDisabledTest {

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class).addClasses(CdiDisabledBean.class, CdiEnabledBean.class)
                .addAsWebInfResource("none-beans.xml", "beans.xml");
    }

    @Inject
    BeanManager beanManager;

    /**
     * The BeanManager should be null.
     */
    @Test
    public void should_bean_manager_be_injected() throws Exception {
        assertTrue("BeanManager shouldn't be present for a CDI disabled archive", beanManager == null);
    }
}
