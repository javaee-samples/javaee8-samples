package org.javaee8.servlet.http2;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test for the HTTP/2 and the JAX-RS client
 */
@RunWith(Arquillian.class)
public class Http2Test {

    @ArquillianResource
    private URL basicUrl;
    private Client jaxrsClient;


    @Deployment
    public static WebArchive createDeployment() {
        final WebArchive war = create(WebArchive.class).addClasses(Servlet.class)
                .addAsWebResource(new File("src/main/webapp/images/payara-logo.jpg"), "images/payara-logo.jpg")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"))
                .addAsResource("project-defaults.yml"); // only for Thormtail;
        System.out.println("War file content: \n" + war.toString(true));
        return war;
    }

    @Before
    public void setup() throws Exception {
        ClientConfig config = new ClientConfig();
        config.connectorProvider(JettyConnector::new);
        jaxrsClient = ClientBuilder.newClient(config);
    }

    @After
    public void cleanUp() throws Exception {
        jaxrsClient.close();
    }


    /**
     * This test runs against the public website supporting HTTP/2
     *
     * @throws Exception
     */
    @Test(timeout = 10000L)
    @RunAsClient
    public void testHttp2ControlGroup() throws Exception {
        Response response = testUri(new URI("https://http2.akamai.com/"));
        assertThat("myproto header", response.getHeaderString("myproto"), Matchers.equalTo("h2"));
    }

    /**
     * This test runs against our private website supporting HTTP/2
     *
     * @throws Exception
     */
    @Test(timeout = 10000L)
    @RunAsClient
    public void testServerHttp2() throws Exception {
        Response response = testUri(basicUrl.toURI());
        // the header 'protocol' is set in the Servlet class.
        assertThat(
            "Request wasn't over HTTP/2. Either the wrong servlet was returned, or the server doesn't support HTTP/2.",
            response.getHeaderString("protocol"), Matchers.equalTo("HTTP/2"));
    }

    private Response testUri(URI uri) {
        Response response = jaxrsClient.target(uri).request().get();
        assertNotNull("response", response);
        return response;
    }
}
