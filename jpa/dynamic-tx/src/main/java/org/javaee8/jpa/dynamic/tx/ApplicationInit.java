package org.javaee8.jpa.dynamic.tx;

import static java.util.stream.Collectors.toSet;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InterceptionFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.javaee8.jpa.dynamic.tx.util.EmployeeBeanWrapper;
import org.javaee8.jpa.dynamic.tx.util.TransactionLiteral;


@Alternative
@Priority(500)
@ApplicationScoped
public class ApplicationInit {
    
    private static final Logger logger = Logger.getLogger(ApplicationInit.class.getName());
    
    @Produces @PersistenceContext
    private EntityManager entityManager;
    
    @Produces
    public EmployeeService produce(InterceptionFactory<EmployeeService> interceptionFactory, BeanManager beanManager) {
        
        logger.info("Producing EmployeeService");
        
        EmployeeService employeeBean =
            createRef(
                beanManager.resolve(
                    beanManager.getBeans(EmployeeService.class)
                               .stream()
                               .filter(e -> !e.getBeanClass().equals(ApplicationInit.class))
                               .collect(toSet())), beanManager);
        
        interceptionFactory
            .configure()
            .filterMethods(am -> am.getJavaMember().getName().equals("persist"))
            .forEach(
                amc -> amc.add(new TransactionLiteral()));
                                   
        return interceptionFactory.createInterceptedInstance(
                new EmployeeBeanWrapper(employeeBean));
    }
    
    EmployeeService createRef(Bean<?> bean, BeanManager beanManager) {
        return (EmployeeService) 
            beanManager.getReference(
                bean, 
                EmployeeService.class, 
                beanManager.createCreationalContext(bean));
    }

}
