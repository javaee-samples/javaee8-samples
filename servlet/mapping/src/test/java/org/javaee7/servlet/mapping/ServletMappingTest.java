package org.javaee7.servlet.mapping;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.javaee8.servlet.mapping.Servlet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class ServletMappingTest {

    @ArquillianResource
    private URL base;

    private WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                    .addClass(Servlet.class);
    }

    @Before
    public void setup() {
        webClient = new WebClient();
    }
    
    @After
    public void teardown() {
        webClient.close();
    }

    @Test
    public void testGet() throws IOException {
        TextPage page = webClient.getPage(base + "foo.ext");
        
        System.out.println(page.getContent());
        
        assertEquals("", "");
    }

   
}
