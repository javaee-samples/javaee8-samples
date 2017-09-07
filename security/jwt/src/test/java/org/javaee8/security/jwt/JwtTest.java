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
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
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
                .addAsWebInfResource("beans.xml");
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
