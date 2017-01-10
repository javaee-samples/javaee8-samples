package org.javaee8.jsonb.mapping;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


@RunWith(Arquillian.class)
public class DogTest {
    @Deployment
    public static Archive<?> deploy() {
        File[] files = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies()
                .resolve().withoutTransitivity().asFile();
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(Dog.class)
                .addAsLibraries(files);
    }

    @Test
    public void test() throws Exception {
        // json text of dog instance
        String jsonDog = "{\"name\":\"Falco\",\"age\":4,\"bitable\":false}";
        // dog instance
        Dog dog = new Dog("Falco", 4, false);

        // create Jsonb and serialize
        Jsonb jsonb = JsonbBuilder.create();

        // deserialize test
        Dog deserializedDog = jsonb.fromJson(jsonDog, Dog.class);
        assertThat(dog, equalTo(deserializedDog));
        // serialization test
        String serializedDog = jsonb.toJson(dog);
        assertThat(deserializedDog, equalTo(jsonb.fromJson(serializedDog, Dog.class)));
    }
}
