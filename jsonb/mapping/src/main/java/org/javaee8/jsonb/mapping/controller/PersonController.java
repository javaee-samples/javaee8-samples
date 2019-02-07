package org.javaee8.jsonb.mapping.controller;

import org.javaee8.jsonb.mapping.domain.Person;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
/**
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getAllPeople() {
        Person person1 = new Person();
        person1.setName("Ondrej");
        person1.setAddress("Prague");
        person1.setPin("Mihalyi");

        Person person2 = new Person();
        person2.setName("Mert");
        person2.setAddress("Turkey");
        person2.setPin("Caliskan");
        
        return Arrays.asList(person1, person2);
    }

}
