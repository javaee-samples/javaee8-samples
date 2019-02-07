package org.javaee8.cdi.dynamic.bean;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
public class ExtensionlessMappingTest {
    
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
                .addClasses(MappingInit.class, ApplicationInit.class, MappingServletContextListener.class)
                .addAsWebResource(new File("src/main/webapp/foo.xhtml"))
                .addAsWebResource(new File("src/main/webapp/bar.xhtml"))
                .addAsWebResource(new File("src/main/webapp/sub/bar.xhtml"), "/sub/bar.xhtml")
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"));
        
        System.out.println("War to be deployed contains: \n" + war.toString(true));
        
        return war;
    }


    @Test
    @RunAsClient
    public void testExtensionlessMappingFoo() throws IOException {
        
        HtmlPage page = webClient.getPage(base + "foo");
        String content = page.asXml();
        
        System.out.println("\nContent for `"+ base + "foo" + "` :\n" + content + "\n");
        
        assertTrue(content.contains("This is page foo"));
    }
    
    @Test
    @RunAsClient
    public void testExtensionlessMappingBar() throws IOException {
        
        HtmlPage page = webClient.getPage(base + "bar");
        String content = page.asXml();
        
        System.out.println("\nContent for `"+ base + "bar" + "` :\n" + content + "\n");
        
        assertTrue(content.contains("This is page bar"));
    }
    
    @Test
    @RunAsClient
    public void testExtensionlessMappingSubBar() throws IOException {
        
        HtmlPage page = webClient.getPage(base + "sub/bar");
        String content = page.asXml();
        
        System.out.println("\nContent for `"+ base + "sub/bar" + "` :\n" + content + "\n");
        
        assertTrue(content.contains("This is page sub-bar"));
    }
    
}
