package org.javaee8.cdi.interception.factory;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InterceptionFactory;

@Alternative
@Priority(500)
@ApplicationScoped
public class MyGreetingProducer {
    
    private static final Logger logger = Logger.getLogger(MyGreetingProducer.class.getName());

    /**
     * This producer produces a MyGreeting alternative for MyGreetingImpl.
     * <p>
     * Note that the alternative is set by making the class, not the method, an
     * alternative.The alternative is activated via the <code>@Priority</code> annotation.
     * 
     * @param interceptionFactory InterceptionFactory injected by CDI
     * @return MyGreeting instance, programmatically proxied
     */
    @Produces
    public MyGreeting produce(InterceptionFactory<MyGreetingImpl> interceptionFactory) {
        
        logger.info("Producing a MyGreeting");
        
        // We're telling the InterceptionFactory here to dynamically add the @HelloAdder
        // annotation.
        interceptionFactory.configure().add(HelloAdder.Literal.INSTANCE);
        
        // This will create a proxy as configured above around the bare
        // instance of MyGreetingImpl that we provide.
        return interceptionFactory.createInterceptedInstance(new MyGreetingImpl());
    }
    
}
