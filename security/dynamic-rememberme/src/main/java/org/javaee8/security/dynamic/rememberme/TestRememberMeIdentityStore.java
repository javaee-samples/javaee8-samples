package org.javaee8.security.dynamic.rememberme;


import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.RememberMeIdentityStore;

@ApplicationScoped
public class TestRememberMeIdentityStore implements RememberMeIdentityStore {

    private Map<String, CredentialValidationResult> loginTokens = new ConcurrentHashMap<>();

    @Override
    public CredentialValidationResult validate(RememberMeCredential credential) {
        if (!loginTokens.containsKey(credential.getToken())) {
            return INVALID_RESULT;
        }
        
        return loginTokens.get(credential.getToken());
    }

    @Override
    public String generateLoginToken(CallerPrincipal callerPrincipal, Set<String> groups) {
        String token = UUID.randomUUID().toString();
        loginTokens.put(token, new CredentialValidationResult(callerPrincipal, groups));

        return token;
    }

    @Override
    public void removeLoginToken(String token) {
        loginTokens.remove(token);
    }

}
