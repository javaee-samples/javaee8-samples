package org.javaee8.servlet.mapping;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.MappingMatch;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Deployment
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
    @RunAsClient
    public void testPath() throws IOException {
        
        // Test Servet is mapped to /path/*, so name after "/path/*" can be anything
        TextPage page = webClient.getPage(base + "path/foo");
        String content = page.getContent();
        
        System.out.println("\nContent for `"+ base + "path/foo" + "` :\n" + content + "\n");
        
        assertTrue(content.contains("Mapping match:" + MappingMatch.PATH.name()));
        assertTrue(content.contains("Match value:'foo'"));
        assertTrue(content.contains("Pattern:'/path/*'"));
    }

    @Test
    @RunAsClient
    public void testExtension() throws IOException {
        
        // Test Servet is mapped to *.ext, so name before ".ext" can be anything
        TextPage page = webClient.getPage(base + "foo.ext");
        String content = page.getContent();
        
        System.out.println("\nContent for `"+ base + "foo.ext" + "` :\n" + content + "\n");
        
        assertTrue(content.contains("Mapping match:" + MappingMatch.EXTENSION.name()));
        assertTrue(content.contains("Match value:'foo'"));
        assertTrue(content.contains("Pattern:'*.ext'"));
    }
    
    @Test
    @RunAsClient
    public void testRoot() throws IOException {
        
        // Test Servet is mapped to the root of the web application
        TextPage page = webClient.getPage(base);
        String content = page.getContent();
        
        System.out.println("\nContent for `"+ base + "` :\n" + content + "\n");
        
        assertTrue(content.contains("Mapping match:" + MappingMatch.CONTEXT_ROOT.name()));
        assertTrue(content.contains("Match value:''"));
        assertTrue(content.contains("Pattern:''"));
    }
    
    @Test
    @RunAsClient
    public void testDefault() throws IOException {
        
        // Test Servet is mapped to the "default", which is a fallback if nothing else matches
        TextPage page = webClient.getPage(base + "doesnotexist");
        String content = page.getContent();
        
        System.out.println("\nContent for `"+ base + "doesnotexist" + "` :\n" + content + "\n");
        
        assertTrue(content.contains("Mapping match:" + MappingMatch.DEFAULT.name()));
        assertTrue(content.contains("Match value:''"));
        assertTrue(content.contains("Pattern:'/'"));
    }
    
    @Test
    @RunAsClient
    public void testExact() throws IOException {
        
        // Test Servet is mapped to an exact name ("/exact"), which is thus not a wildcard of any kind
        TextPage page = webClient.getPage(base + "exact");
        String content = page.getContent();
        
        System.out.println("\nContent for `"+ base + "exact" + "` :\n" + content + "\n");
        
        assertTrue(content.contains("Mapping match:" + MappingMatch.EXACT.name()));
        assertTrue(content.contains("Match value:'exact'"));
        assertTrue(content.contains("Pattern:'/exact'"));
    }
    

   
}
