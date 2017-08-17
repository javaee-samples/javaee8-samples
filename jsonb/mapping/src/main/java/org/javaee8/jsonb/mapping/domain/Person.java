package org.javaee8.jsonb.mapping.domain;

import java.io.Serializable;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;

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
    
    /**
     * Property 'pin' will be ignored by JSON Binding engine
     */
    @JsonbTransient
    private String pin;
    

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

    public String getPin() {
        return pin;
    }
    
    public void setPin(String pin) {
        this.pin = pin;
    }

}