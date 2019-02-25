/** Copyright Payara Services Limited **/
package org.javaee8;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.SECURITY_CREDENTIALS;
import static javax.naming.Context.SECURITY_PRINCIPAL;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * This class returns a JNDI context suitable for remote lookups against the default URL
 * for a remote Payara or GlassFish server (localhost). It sets the provided credentials
 * in a Payara/GlassFish specific way.
 *
 * @author Arjan Tijms
 *
 */
public class PayaraEJBContextProvider implements RemoteEJBContextProvider {
    public Context getContextWithCredentialsSet(String username, String password) {
        Hashtable<String, String> environment = new Hashtable<String, String>();
        environment.put(INITIAL_CONTEXT_FACTORY, "fish.payara.ejb.rest.client.RemoteEJBContextFactory");
        environment.put(PROVIDER_URL, "http://localhost:8080/ejb-invoker");
        environment.put(SECURITY_PRINCIPAL, "u1");
        environment.put(SECURITY_CREDENTIALS, "p1");
        
        try {
            return new InitialContext(environment);
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
        
    }

    public void releaseContext() {
    }

}
