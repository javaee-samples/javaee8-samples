package org.javaee8.cdi.dynamic.bean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

/**
 * 
 * @author Arjan Tijms
 *
 */
public class CdiExtension implements Extension {

    public void afterBean(final @Observes AfterBeanDiscovery afterBeanDiscovery) {
        afterBeanDiscovery
            .addBean()
            .scope(ApplicationScoped.class)
            .types(MyBean.class)
            .id("Created by " + CdiExtension.class)
            .createWith(e -> new MyBeanImpl("Hi!"));
    }
    
}
