package org.javaee8.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author mertcaliskan
 */
public class Country {

    @NotNull
    @Size(min = 2, max = 2)
    private String countryCode;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}