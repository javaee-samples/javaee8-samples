/** Copyright Payara Services Limited **/
package org.javaee8;

import java.util.Iterator;
import java.util.ServiceLoader;

public class RemoteEJBContextFactory {

    public static RemoteEJBContextProvider getProvider() {

        ServiceLoader<RemoteEJBContextProvider> loader = ServiceLoader.load(RemoteEJBContextProvider.class);

        Iterator<RemoteEJBContextProvider> providers = loader.iterator();

        if (!providers.hasNext()) {
            return null;
        }

        return providers.next();

    }

}
