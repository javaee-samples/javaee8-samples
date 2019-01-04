/*
 * Copyright (c) 2017 Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/master/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * The Payara Foundation designates this particular file as subject to the "Classpath"
 * exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.javaee8.security.jwt;

import java.io.File;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import java.io.IOException;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.net.URISyntaxException;
import javax.ws.rs.client.ClientBuilder;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import static org.javaee8.security.jwt.Constants.AUTHORIZATION_HEADER;
import org.javaee8.security.jwt.rest.ApplicationConfig;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Gaurav Gupta
 */
@RunWith(Arquillian.class)
public class JwtTest {

    @ArquillianResource
    private URL base;

    private WebTarget webTarget;

    @Deployment
    public static WebArchive createDeployment() {
        MavenResolverSystem RESOLVER = Maven.resolver();
        File[] jjwtFiles = RESOLVER.resolve("io.jsonwebtoken:jjwt:0.6.0").withTransitivity().asFile();

        return create(WebArchive.class)
                .addPackage(ApplicationConfig.class.getPackage())
                .addPackage(JWTCredential.class.getPackage())
                .addAsLibraries(jjwtFiles)
                .setWebXML("web.xml")
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-web.xml");
    }

    @Before
    public void setup() throws URISyntaxException {
        webTarget = ClientBuilder.newClient().target(base.toURI().toString() + "api/");
    }

    @Test
    @RunAsClient
    public void testNotAuthenticated() throws IOException {
        Response response = webTarget
                .path("auth/login")
                .queryParam("name", "duke")
                .queryParam("password", "invalid")
                .queryParam("rememberme", "false")
                .request()
                .get();
        String authorizationHeader = response.getHeaderString(AUTHORIZATION_HEADER);
        assertNull(authorizationHeader);
        assertEquals(
                401,
                response.getStatus());
    }

    @Test
    @RunAsClient
    public void testAuthenticatedWithoutRememberme() throws IOException {
        Response response = webTarget
                .path("auth/login")
                .queryParam("name", "duke")
                .queryParam("password", "secret")
                .queryParam("rememberme", "false")
                .request()
                .get();
        String authorizationHeader = response.getHeaderString(AUTHORIZATION_HEADER);
        assertNotNull(authorizationHeader);
        assertEquals(
                200,
                response.getStatus());

        Response protectedResponse = webTarget
                .path("sample/write")
                .request()
                .header(AUTHORIZATION_HEADER, authorizationHeader)
                .post(json(null));
        assertEquals(
                200,
                protectedResponse.getStatus());
        
        Response adminResponse = webTarget
                .path("sample/delete")
                .request()
                .header(AUTHORIZATION_HEADER, authorizationHeader)
                .delete();
        //Only ROLE_ADMIN user can access
        assertEquals(
                403,
                adminResponse.getStatus());

    }

    @Test
    @RunAsClient
    public void testAuthenticatedWithRememberme() throws IOException {
        Response response = webTarget
                .path("auth/login")
                .queryParam("name", "payara")
                .queryParam("password", "fish")
                .queryParam("rememberme", "true")
                .request()
                .get();
        String token = response.getCookies().get("JREMEMBERMEID").getValue();
        assertNotNull(token);
        assertEquals(
                200,
                response.getStatus());

        Response protectedResponse = webTarget
                .path("sample/write")
                .request()
                .cookie("JREMEMBERMEID", token)
                .post(json(null));
        assertEquals(
                200,
                protectedResponse.getStatus());

        Response adminResponse = webTarget
                .path("sample/delete")
                .request()
                .cookie("JREMEMBERMEID", token)
                .delete();
        assertEquals(
                200,
                adminResponse.getStatus());

    }
}
