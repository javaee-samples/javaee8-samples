package org.javaee8.servlet.push;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;

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

import com.gargoylesoftware.htmlunit.WebClient;
import javax.servlet.annotation.WebServlet;

@RunWith(Arquillian.class)
public class ServletPushTest {

    @ArquillianResource
    private URL base;

    private Page servletPage;

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addClass(Servlet.class);
    }

    @Before
    public void setup() {
        servletPage = null;
        try (WebClient webClient = new WebClient()) {
            servletPage = webClient.getPage(base + Servlet.class.getDeclaredAnnotation(WebServlet.class).value()[0]);
        } catch (IOException | FailingHttpStatusCodeException ex) {
            System.err.println("Error getting servlet page.\n" + ex.getMessage());
        }
    }

    @Test
    @RunAsClient
    public void testPush() throws IOException {
        String content = servletPage.getWebResponse().getContentAsString();

        assertTrue("The page was delivered using HTTP 1.1.", content.contains("HTTP 2"));
    }

}
