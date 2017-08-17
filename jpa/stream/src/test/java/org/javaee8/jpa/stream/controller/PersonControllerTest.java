package org.javaee8.jpa.stream.controller;

import java.util.stream.Stream;
import org.javaee8.jpa.stream.repository.PersonRepository;
import org.javaee8.jpa.stream.domain.Person;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Test;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

/**
 * 
 * @author Gaurav Gupta
 *
 */
@RunWith(Arquillian.class)
public class PersonControllerTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addAsWebInfResource("beans.xml")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/sql/insert.sql")
                .addClass(Person.class)
                .addClass(PersonRepository.class);
    }

    @Inject
    private PersonRepository personRepository;

    @Test
    public void testStream() throws Exception {
        Stream<Person> personStream = personRepository.findAll();
        long personCount = personStream.count();
        assertEquals(2, personCount);
    }

}
