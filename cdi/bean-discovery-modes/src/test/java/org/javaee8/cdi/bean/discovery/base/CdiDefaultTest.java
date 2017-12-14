package org.javaee8.cdi.bean.discovery.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
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
public class CdiDefaultTest {

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class).addClasses(CdiDisabledBean.class, CdiEnabledBean.class);
    }

    @Inject
    BeanManager beanManager;

    /**
     * Should work the same as annotated.
     */
    @Test
    public void should_beans_be_injected() throws Exception {
        Set<Bean<?>> disabledBeans = beanManager.getBeans(CdiDisabledBean.class);
        assertTrue("No instances of disabled bean expected.", disabledBeans.isEmpty());

        Set<Bean<?>> enabledBeans = beanManager.getBeans(CdiEnabledBean.class);
        assertFalse("Instances of enabled bean expected.", enabledBeans.isEmpty());
    }
}
