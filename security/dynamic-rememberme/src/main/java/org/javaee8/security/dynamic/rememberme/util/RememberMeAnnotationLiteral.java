package org.javaee8.security.dynamic.rememberme.util;

import javax.enterprise.util.AnnotationLiteral;
import javax.security.enterprise.authentication.mechanism.http.RememberMe;

@SuppressWarnings("all")
public class RememberMeAnnotationLiteral extends AnnotationLiteral<RememberMe> implements RememberMe {
        
    private static final long serialVersionUID = 1L;
    
        int cookieMaxAgeSeconds;
        String cookieMaxAgeSecondsExpression;
        boolean cookieSecureOnly;
        String cookieSecureOnlyExpression;
        boolean cookieHttpOnly;
        String cookieHttpOnlyExpression;
        String cookieName;
        boolean isRememberMe;
        String isRememberMeExpression;

    public RememberMeAnnotationLiteral(
        
        int cookieMaxAgeSeconds,
        String cookieMaxAgeSecondsExpression,
        boolean cookieSecureOnly,
        String cookieSecureOnlyExpression,
        boolean cookieHttpOnly,
        String cookieHttpOnlyExpression,
        String cookieName,
        boolean isRememberMe,
        String isRememberMeExpression
        
            ) {
        
        this.cookieMaxAgeSeconds =              cookieMaxAgeSeconds;
        this.cookieMaxAgeSecondsExpression =    cookieMaxAgeSecondsExpression;
        this.cookieSecureOnly =                 cookieSecureOnly;
        this.cookieSecureOnlyExpression =       cookieSecureOnlyExpression;
        this.cookieHttpOnly =                   cookieHttpOnly;
        this.cookieHttpOnlyExpression =         cookieHttpOnlyExpression;
        this.cookieName =                       cookieName;
        this.isRememberMe =                     isRememberMe;
        this.isRememberMeExpression =           isRememberMeExpression;
    }
    
    @Override
    public boolean cookieHttpOnly() {
        return cookieHttpOnly;
    }
    
    @Override
    public String cookieHttpOnlyExpression() {
        return cookieHttpOnlyExpression;
    }
    
    @Override
    public int cookieMaxAgeSeconds() {
        return cookieMaxAgeSeconds;
    }
    
    @Override
    public String cookieMaxAgeSecondsExpression() {
        return cookieMaxAgeSecondsExpression;
    }

    @Override
    public boolean cookieSecureOnly() {
        return cookieSecureOnly;
    }

    @Override
    public String cookieSecureOnlyExpression() {
        return cookieSecureOnlyExpression;
    }

    @Override
    public String cookieName() {
        return cookieName;
    }
    
    public boolean isRememberMe() {
        return isRememberMe;
    }

    @Override
    public String isRememberMeExpression() {
        return isRememberMeExpression;
    }
}