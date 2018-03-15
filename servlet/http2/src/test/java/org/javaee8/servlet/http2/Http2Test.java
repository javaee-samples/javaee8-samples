package org.javaee8.servlet.http2;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

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

    private WebClient client;

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
        String url = "https://http2.akamai.com/";
        assertNotNull(client.getResponse(url));
    }

    @Test
    @RunAsClient
    public void testServerHttp2(@ArquillianResource URL url)
            throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        assertNotNull(client.getResponse(url.toString()));
    }

    @Before
    public void setup() throws Exception {
        client = new WebClient(Level.INFO);
        client.start();
    }

    @After
    public void cleanUp() throws Exception {
        client.stop();
    }

}
