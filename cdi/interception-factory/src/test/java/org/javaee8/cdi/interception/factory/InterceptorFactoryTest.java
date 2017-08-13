package org.javaee8.cdi.interception.factory;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class InterceptorFactoryTest {
    
    @Deployment
    public static Archive<?> deploy() {
        return create(JavaArchive.class)
                .addClasses(
                    MyGreeting.class, MyGreetingImpl.class, MyGreetingProducer.class, 
                    HelloAdder.class, HelloAdderInterceptor.class)
                .addAsManifestResource("beans.xml");
    }

    @Inject
    private MyGreeting myGreeting;

    @Test
    public void test() {
        myGreeting.setGreet("Reza");
        
        assertEquals("Hello Reza", myGreeting.getGreet());
    }
}
