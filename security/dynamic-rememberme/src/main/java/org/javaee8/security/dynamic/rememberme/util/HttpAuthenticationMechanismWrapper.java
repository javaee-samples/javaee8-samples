package org.javaee8.security.dynamic.rememberme.util;

import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpAuthenticationMechanismWrapper implements HttpAuthenticationMechanism {

    private HttpAuthenticationMechanism httpAuthenticationMechanism;

    public HttpAuthenticationMechanismWrapper() {
    }

    public HttpAuthenticationMechanismWrapper(HttpAuthenticationMechanism httpAuthenticationMechanism) {
        this.httpAuthenticationMechanism = httpAuthenticationMechanism;
    }

    HttpAuthenticationMechanism getWrapped() {
        return httpAuthenticationMechanism;
    }

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
            HttpMessageContext httpMessageContext) throws AuthenticationException {
        return getWrapped().validateRequest(request, response, httpMessageContext);
    }

    @Override
    public AuthenticationStatus secureResponse(HttpServletRequest request, HttpServletResponse response,
            HttpMessageContext httpMessageContext) throws AuthenticationException {
        return getWrapped().secureResponse(request, response, httpMessageContext);
    }

    @Override
    public void cleanSubject(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        getWrapped().cleanSubject(request, response, httpMessageContext);
    }

}