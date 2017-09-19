package org.javaee8.cdi.qualified.lookup;

import static java.lang.System.out;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.literal.NamedLiteral;
import javax.enterprise.inject.spi.CDI;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class QualifiedLookupTest {
    
    @Deployment
    public static Archive<?> deploy() {
        return create(JavaArchive.class)
                .addClasses(
                    MyGreeting.class, MyGreeting1.class, MyGreeting2.class)
                .addAsManifestResource("beans.xml");
    }

    @Test
    public void test() {
        
        Instance<MyGreeting> myGreetings = CDI.current().select(MyGreeting.class);
        
        myGreetings.stream()
                   .forEach(e -> out.println(e.getGreet()));
        
        assertEquals("Ay-up!", myGreetings.select(NamedLiteral.of("northern")).get().getGreet());
        
        assertEquals("Hiya!", myGreetings.select(NamedLiteral.of("informal")).get().getGreet());
        
        assertEquals(false, myGreetings.select(NamedLiteral.of("formal")).isResolvable());
    }
}
