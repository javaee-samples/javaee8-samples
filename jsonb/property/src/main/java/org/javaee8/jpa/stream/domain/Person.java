package org.javaee8.jpa.stream.domain;

import java.io.Serializable;
import javax.json.bind.annotation.JsonbProperty;

/**
 * 
 * @author Gaurav Gupta
 *
 */
public class Person implements Serializable {

    /**
     * JsonbProperty is used to change name of one particular property.
     * Property 'name' will be serialized to 'pname' property
     */
    @JsonbProperty("pname")
    private String name;

    private String address;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}