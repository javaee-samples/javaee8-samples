/** Copyright Payara Services Limited **/
package org.javaee8.jsf.hello.world;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.javaee8.jsf.hello.world.ApplicationInit;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 *
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class JSFHelloWorldTest {

    @ArquillianResource
    private URL base;

    private WebClient webClient;

    @Before
    public void setup() {
        webClient = new WebClient();
    }

    @After
    public void teardown() {
        webClient.close();
    }

    @Deployment
    public static WebArchive deploy() {
        WebArchive war =
            create(WebArchive.class)
                .addClasses(ApplicationInit.class, HelloBacking.class)
                .addAsWebResource(new File("src/main/webapp/hello.xhtml"))
                ;

        System.out.println("War to be deployed contains: \n" + war.toString(true));

        return war;
    }


    @Test
    @RunAsClient
    public void testHelloWorld() throws IOException {
        HtmlPage page = webClient.getPage(base + "hello.xhtml");

        assertTrue(page.asXml().contains("Hello world, from JSF!"));
    }



}
