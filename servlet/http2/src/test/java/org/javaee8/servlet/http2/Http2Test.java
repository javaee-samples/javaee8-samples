package org.javaee8.servlet.http2;

import static org.eclipse.jetty.http.HttpVersion.HTTP_2;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;
import org.eclipse.jetty.util.ssl.SslContextFactory;
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

    private HttpClient httpClient;

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addPackages(true, "org.javaee8.servlet.http2")
                .addAsWebResource(new File("src/main/webapp/images/payara-logo.jpg"), "images/payara-logo.jpg")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"));
    }

    @Test
    @RunAsClient
    public void testHttp2ControlGroup() {
        String url = "https://javaee.github.io/";
        testUrl(url);
    }

    @Test
    @RunAsClient
    public void testServerHttp2(@ArquillianResource URL base) {
        String url = String.format("https://%s:%d%s", base.getHost(), 8181, base.getPath());
        testUrl(url);
    }

    private void testUrl(String url) {
        System.out.println("Testing url: " + url);
        ContentResponse response = null;
        try {
            response = httpClient.GET(url);
        } catch (InterruptedException ex) {
            fail("Request was interruped with exception: " + ex.getMessage());
        } catch (ExecutionException ex) {
            fail("Exception whilst executing request: " + ex.getMessage());
        } catch (TimeoutException ex) {
            fail("Request timed out with exception: " + ex.getMessage());
        }

        assertFalse("Error getting the response.", response == null);

        HttpVersion protocol = response.getVersion();

        assertTrue(String.format("The page was delivered over %s instead of HTTP 2.", protocol.asString()),
                protocol.equals(HTTP_2));
    }

    @Before
    public void setup() throws Exception {
        //System.setProperty("org.eclipse.jetty.client.LEVEL", "DEBUG");
        SslContextFactory sslContextFactory = new SslContextFactory(true);
        HTTP2Client http2Client = new HTTP2Client();
        HttpClientTransportOverHTTP2 http2Transport = new HttpClientTransportOverHTTP2(http2Client);
        httpClient = new HttpClient(http2Transport, sslContextFactory);
        httpClient.start();
    }

    @After
    public void cleanUp() throws Exception {
        httpClient.stop();
    }

}
