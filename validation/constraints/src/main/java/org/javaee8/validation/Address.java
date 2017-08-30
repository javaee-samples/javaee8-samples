package org.javaee8.validation;

import javax.validation.constraints.NotEmpty;

/**
 * @author mertcaliskan
 */
public class Address {

    @NotEmpty
    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}