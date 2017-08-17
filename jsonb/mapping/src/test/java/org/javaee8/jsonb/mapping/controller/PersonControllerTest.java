package org.javaee8.jsonb.mapping.controller;

import org.javaee8.jsonb.mapping.controller.ApplicationConfig;
import org.javaee8.jsonb.mapping.controller.PersonController;
import java.net.URI;
import java.net.URL;
import java.util.List;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.javaee8.jsonb.mapping.domain.Person;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Test;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 *
 * @author Gaurav Gupta
 *
 */
@RunWith(Arquillian.class)
public class PersonControllerTest {

    private static final String RESOURCE_PATH = "api/person";

    @ArquillianResource
    private URL base;

    private static WebTarget target;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(Person.class)
                .addClass(PersonController.class)
                .addClass(ApplicationConfig.class);
    }

    @Before
    public void setUpClass() throws Exception {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "resources/").toExternalForm()));
    }

    @Test
    public void testJSONB() throws Exception {
        Jsonb jsonb = JsonbBuilder.create();
        // Get all the people
        Response response = target.path(RESOURCE_PATH).request().get();
        String val = jsonb.toJson(response.readEntity(List.class));
        assertEquals("[{\"address\":\"Prague\",\"pname\":\"Ondrej\"},{\"address\":\"Turkey\",\"pname\":\"Mert\"}]", val);
    }

}
