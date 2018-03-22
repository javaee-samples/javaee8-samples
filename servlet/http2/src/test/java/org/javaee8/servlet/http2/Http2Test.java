package org.javaee8.servlet.http2;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class Http2Test {

    private Client jaxrsClient;

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class).addPackages(true, "org.javaee8.servlet.http2")
                .addAsWebResource(new File("src/main/webapp/images/payara-logo.jpg"), "images/payara-logo.jpg")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"));
    }

    @Test
    @RunAsClient
    public void testHttp2ControlGroup()
            throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        testUrl("https://http2.akamai.com/");
    }

    @Test
    @RunAsClient
    public void testServerHttp2(@ArquillianResource URL url)
            throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        Response response = testUrl(url.toURI().toString());
        assertEquals(
                "Request wasn't over HTTP/2."
                        + " Either the wrong servlet was returned, or the server doesn't support HTTP/2.",
                response.getHeaderString("protocol"), "HTTP/2");
    }

    private Response testUrl(String url) {
        Response response = jaxrsClient.target(url).request().get();
        assertNotNull(response);
        return response;
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

}
