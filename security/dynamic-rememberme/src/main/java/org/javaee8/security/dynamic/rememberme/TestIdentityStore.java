package org.javaee8.security.dynamic.rememberme;

import static java.util.Arrays.asList;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

import java.util.HashSet;

import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

@RequestScoped
public class TestIdentityStore implements IdentityStore {

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {

        if (!(credential.getCaller().equals("test") && credential.getPassword().compareTo("pass"))) {
            return INVALID_RESULT;
        }
        
        return new CredentialValidationResult("test", new HashSet<>(asList("architect", "admin")));
    }

}
