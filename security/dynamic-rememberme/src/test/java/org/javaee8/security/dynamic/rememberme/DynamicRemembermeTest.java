package org.javaee8.security.dynamic.rememberme;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.apache.http.client.CredentialsProvider;
import org.javaee8.security.dynamic.rememberme.util.RememberMeAnnotationLiteral;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class DynamicRemembermeTest {

    @ArquillianResource
    private URL base;

    private WebClient webClient;

    @Deployment
    public static WebArchive createDeployment() {
        return 
            create(WebArchive.class)
                .addClasses(
                    ApplicationInit.class,
                    TestIdentityStore.class,
                    TestRememberMeIdentityStore.class,
                    Servlet.class
                )
                .addPackage(
                    RememberMeAnnotationLiteral.class.getPackage())
                .addAsWebInfResource("jboss-web.xml");
    }

    @Before
    public void setup() {
        webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    }
    
    @After
    public void teardown() {
        webClient.close();
    }
    
    @Test
    @RunAsClient
    public void testNotAuthenticated() throws IOException {
        Page page = webClient.getPage(base + "servlet");
        
        assertEquals(
            401, 
            page.getWebResponse().getStatusCode());
        
        assertTrue(
            "Response did not contain the \"WWW-Authenticate\" header.", 
            page.getWebResponse().getResponseHeaderValue("WWW-Authenticate") != null);
    }
    
    @Test
    @RunAsClient
    public void testAuthenticatedOnce() throws IOException {
        
        // 1. Request the resource with valid HTTP BASIC credentials
        
        CredentialsProvider originalCredentialsProvider = webClient.getCredentialsProvider();
        DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
        credentialsProvider.addCredentials("test", "pass");
        
        webClient.setCredentialsProvider(credentialsProvider);
        
        Page page = webClient.getPage(base + "servlet");
        
        // We should now get a 200 (OK) response
        
        assertEquals(
            200, 
            page.getWebResponse().getStatusCode());
        
        String content = page.getWebResponse().getContentAsString();
        
        assertTrue(
            content.contains("web username: test"));
        
        assertTrue(
            content.contains("web user has role \"architect\": true"));
        
          
        // Because remember-me is used, we should have received the JREMEMBERMEID cookie

        Cookie cookie = webClient.getCookieManager().getCookie("JREMEMBERMEID");

        assertNotNull(cookie);

        System.out.println("JREMEMBERMEID cookie: " + cookie);

        
      
        // 2. Request the resource without valid HTTP BASIC credentials

        webClient.setCredentialsProvider(originalCredentialsProvider);

        page = webClient.getPage(base + "servlet");

        // Since our credentials have been exchanged for a token (residing within the cookie)
        // we should still be able to get a 200
      
        assertEquals(200, page.getWebResponse().getStatusCode());
      
        content = page.getWebResponse().getContentAsString();
        
        assertTrue(
                content.contains("web username: test"));
            
        assertTrue(
            content.contains("web user has role \"architect\": true"));
        
        System.out.println("\nContent:\n" + content + "\n");

        
        
        // 3. Request the resource without valid HTTP BASIC credentials as well as without the cookie

        webClient.getCookieManager().removeCookie(cookie);

        page = webClient.getPage(base + "servlet");

        // We should not get a 200 okay now but a 401 Unauthorized, and the server should
        // ask for our credentials again

        assertEquals(
            401, 
            page.getWebResponse().getStatusCode());

        assertTrue(
            "Response did not contain the \"WWW-Authenticate\" header.",
             page.getWebResponse().getResponseHeaderValue("WWW-Authenticate") != null);

    }

}
