package org.javaee8.jpa.stream.controller;

import org.javaee8.jpa.stream.domain.Person;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * REST controller for managing Person.
 * 
 * @author Gaurav Gupta
 */
@Path("/api/person")
public class PersonController {

    /**
     * GET : get all the people.
     *
     * @return the Response with status 200 (OK) and the list of people in body
     *
     */
    @GET
    public List<Person> getAllPeople() {
        Person person1 = new Person();
        person1.setName("Ondrej");
        person1.setAddress("xyz");

        Person person2 = new Person();
        person2.setName("Mert");
        person2.setAddress("pqrs");
        
        return Arrays.asList(person1, person2);
    }

}
