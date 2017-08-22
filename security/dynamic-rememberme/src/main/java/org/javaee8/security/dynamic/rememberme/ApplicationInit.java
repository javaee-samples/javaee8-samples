package org.javaee8.security.dynamic.rememberme;

import static java.util.stream.Collectors.toSet;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InterceptionFactory;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;

import org.javaee8.security.dynamic.rememberme.util.HttpAuthenticationMechanismWrapper;
import org.javaee8.security.dynamic.rememberme.util.RememberMeAnnotationLiteral;

// Configure a basic authentication mechanism.
// In this sample we'll dynamically apply the @RememberMe annotation to this.
@BasicAuthenticationMechanismDefinition(
    realmName="foo"
)

@Alternative
@Priority(500)
@ApplicationScoped
public class ApplicationInit {
    
    private static final Logger logger = Logger.getLogger(ApplicationInit.class.getName());
    
    @Produces
    public HttpAuthenticationMechanism produce(InterceptionFactory<HttpAuthenticationMechanismWrapper> interceptionFactory, BeanManager beanManager) {
        
        logger.info("Producing wrapped and dynamic proxied mechanism");
        
        // Get a reference (instance) to the HttpAuthenticationMechanism that would have been
        // used had we not provided an alternative here. 
        //
        // In this sample that is the mechanism
        // the container puts into service following the @BasicAuthenticationMechanismDefinition
        // used above.
        HttpAuthenticationMechanism mechanism =
            createRef(
                beanManager.resolve(
                    beanManager.getBeans(HttpAuthenticationMechanism.class)
                               .stream()
                               .filter(e -> !e.getBeanClass().equals(ApplicationInit.class))
                               .collect(toSet())), beanManager);
        
        // We're telling the InterceptionFactory here to dynamically add the @RememberMeAnnotation
        // annotation with the supplied values.
        interceptionFactory.configure().add(
            new RememberMeAnnotationLiteral(
                86400, "",
                false, "",
                true, "",
                "JREMEMBERMEID",
                true, ""
            )
        );
        
        // This will create a proxy as configured above around an instance of 
        // HttpAuthenticationMechanismWrapper that we provide.
        
        // Note that we provide an extra wrapper since unfortunately createInterceptedInstance
        // says: 
        // "If the provided instance is an internal container construct (such as client proxy), non-portable behavior results."
        return interceptionFactory.createInterceptedInstance(
            new HttpAuthenticationMechanismWrapper(mechanism));
    }
    
    HttpAuthenticationMechanism createRef(Bean<?> bean, BeanManager beanManager) {
        return (HttpAuthenticationMechanism) 
            beanManager.getReference(
                bean, 
                HttpAuthenticationMechanism.class, 
                beanManager.createCreationalContext(bean));
    }

}
