/** Copyright Payara Services Limited **/
package org.javaee8.ejb.remote.remote;

import javax.ejb.Remote;

@Remote
public interface BeanRemote {
    String method();
}
