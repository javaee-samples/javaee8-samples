package org.javaee8.jsonp.pointer;

import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonMergePatch;
import javax.json.JsonObject;
import javax.json.JsonPointer;
import javax.json.JsonValue;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class that tests and demonstrates the JSON-P 1.1 Pointer Operations.
 * @author Andrew Pielage
 */
@RunWith(Arquillian.class)
public class JsonpPointerTest {
    
    // Create a JsonObject with some values to be used in each test
    private static final JsonObject json = Json.createObjectBuilder()
            .add("Wibbly", "Wobbly")
            .add("Replaced", false)
            .add("Lexicon", Json.createArrayBuilder()
                    .add("Wibbles")
                    .add("Wobbles")
                    .build())
            .add("Nested", Json.createObjectBuilder()
                    .add("Birdie", "Wordie")
                    .add("Bestiary", Json.createArrayBuilder()
                            .add("Drowner")
                            .add("Werewolf")
                            .add("Chimera")
                            .build())
                    .build())
            .build();
    
    @Deployment
    public static JavaArchive createDeployment() {
        // Create a JavaArchive to deploy
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class);

        // Print out directory contents
        System.out.println(jar.toString(true));

        // Return Arquillian Test Archive for application server
        return jar;
    }
    
    /**
     * Test that the JSON Pointers resolve to the correct values as.
     */
    @Test
    public void resolveTest() {
        // Create pointers
        JsonPointer objectPointer = Json.createPointer("");
        JsonPointer objectMemberPointer = Json.createPointer("/Wibbly");
        JsonPointer arrayPointer = Json.createPointer("/Lexicon");
        JsonPointer arrayElementPointer = Json.createPointer("/Lexicon/0");
        JsonPointer nestedObjectPointer = Json.createPointer("/Nested");
        JsonPointer nestedObjectMemberPointer = Json.createPointer("/Nested/Birdie");
        JsonPointer nestedArrayPointer = Json.createPointer("/Nested/Bestiary");
        JsonPointer nestedArrayElementPointer = Json.createPointer("/Nested/Bestiary/1");
        
        // Check pointers return the correct values
        assertTrue("objectPointer doesn't resolve correctly!", objectPointer.getValue(json).equals(json));
        assertTrue("objectMemberPointer doesn't resolve correctly!", 
                objectMemberPointer.getValue(json).toString().equals("\"Wobbly\""));
        assertTrue("arrayPointer doesn't resolve correctly!", 
                arrayPointer.getValue(json).equals(json.getJsonArray("Lexicon")));
        assertTrue("arrayElementPointer doesn't resolve correctly!", 
                arrayElementPointer.getValue(json).toString().equals("\"Wibbles\""));
        assertTrue("nestedObjectPointer doesn't resolve correctly!", 
                nestedObjectPointer.getValue(json).equals(json.getJsonObject("Nested")));
        assertTrue("nestedObjectMemberPointer doesn't resolve correctly!", 
                nestedObjectMemberPointer.getValue(json).toString().equals("\"Wordie\""));
        assertTrue("nestedArrayPointer doesn't resolve correctly!", 
                nestedArrayPointer.getValue(json).equals(json.getJsonObject("Nested").getJsonArray("Bestiary")));
        assertTrue("nestedArrayElementPointer doesn't resolve correctly!", 
                nestedArrayElementPointer.getValue(json).toString().equals("\"Werewolf\""));
        
        // Check alternative notation
        assertTrue("objectMemberPointer doesn't resolve correctly!", json.getValue("/Wibbly").toString().equals("\"Wobbly\""));
        assertTrue("objectMemberPointer doesn't resolve correctly!", 
                json.getValue("/Nested/Bestiary/2").toString().equals("\"Chimera\""));
    }
    
    /**
     * Test that the JSON Pointer add operation works as expected.
     */
    @Test
    public void addTest() {
        // Create pointers
        JsonPointer objectMemberPointer = Json.createPointer("/Giggly");
        JsonPointer arrayElementPointer = Json.createPointer("/Lexicon/0");
        JsonPointer nestedObjectMemberPointer = Json.createPointer("/Nested/Purdie");
        JsonPointer nestedArrayElementPointer = Json.createPointer("/Nested/Bestiary/1");
        
        // Perform an add operation on each pointer
        JsonObject modifiedJson = objectMemberPointer.add(json, Json.createValue("Goggly"));
        modifiedJson = arrayElementPointer.add(modifiedJson, Json.createValue("Giggles"));
        modifiedJson = nestedObjectMemberPointer.add(modifiedJson, Json.createValue("Furdie"));
        modifiedJson = nestedArrayElementPointer.add(modifiedJson, Json.createValue("Wraith"));
        
        // Check that they've been added
        assertTrue("Object member not added!", modifiedJson.containsKey("Giggly") 
                && modifiedJson.getString("Giggly").equals("Goggly"));
        assertTrue("Array element not added!", modifiedJson.getJsonArray("Lexicon").size() == 3
                && modifiedJson.getJsonArray("Lexicon").getString(0).equals("Giggles"));
        assertTrue("Nested object member not added!", modifiedJson.getJsonObject("Nested").containsKey("Purdie") 
                && modifiedJson.getJsonObject("Nested").getString("Purdie").equals("Furdie"));
        assertTrue("Nested array element not added!", 
                modifiedJson.getJsonObject("Nested").getJsonArray("Bestiary").size() == 4
                && modifiedJson.getJsonObject("Nested").getJsonArray("Bestiary").getString(1).equals("Wraith"));
    }
    
    /**
     * Test that the JSON Pointer remove operation works as expected.
     */
    @Test
    public void removeTest() {
        // Create pointers
        JsonPointer objectMemberPointer = Json.createPointer("/Wibbly");
        JsonPointer arrayElementPointer = Json.createPointer("/Lexicon/0");
        JsonPointer nestedObjectMemberPointer = Json.createPointer("/Nested/Birdie");
        JsonPointer nestedArrayPointer = Json.createPointer("/Nested/Bestiary");
        
        // Perform a remove operation using each pointer
        JsonObject modifiedJson = objectMemberPointer.remove(json);
        modifiedJson = arrayElementPointer.remove(modifiedJson);
        modifiedJson = nestedObjectMemberPointer.remove(modifiedJson);
        modifiedJson = nestedArrayPointer.remove(modifiedJson);
        
        // Check that they've been removed
        assertTrue("Object member not removed!", !modifiedJson.containsKey("Wibbly"));
        assertTrue("Array element not removed!", modifiedJson.getJsonArray("Lexicon").size() == 1
                && !modifiedJson.getJsonArray("Lexicon").getString(0).equals("Drowner"));
        assertTrue("Nested object member not removed!", !modifiedJson.getJsonObject("Nested").containsKey("Birdie"));
        assertTrue("Nested array not removed!", !modifiedJson.getJsonObject("Nested").containsKey("Bestiary"));
    }
    
    /**
     * Test that the JSON Pointer replace operation works as expected.
     */
    @Test
    public void replaceTest() {
        // Create pointers
        JsonPointer objectMemberPointer = Json.createPointer("/Wibbly");
        JsonPointer arrayElementPointer = Json.createPointer("/Lexicon/0");
        JsonPointer nestedObjectMemberPointer = Json.createPointer("/Nested/Birdie");
        
        // Perform a replace operation using each pointer
        JsonObject modifiedJson = objectMemberPointer.replace(json, Json.createValue("Bobbly"));
        modifiedJson = arrayElementPointer.replace(modifiedJson, Json.createValue("Tiddles"));
        modifiedJson = nestedObjectMemberPointer.replace(modifiedJson, Json.createValue("Bubbly"));
        
        // Check that they've been replaced
        assertTrue("Object member not added!", modifiedJson.containsKey("Wibbly") 
                && modifiedJson.getString("Wibbly").equals("Bobbly"));
        assertTrue("Array element not added!", modifiedJson.getJsonArray("Lexicon").size() == 2
                && modifiedJson.getJsonArray("Lexicon").getString(0).equals("Tiddles"));
        assertTrue("Nested object member not added!", modifiedJson.getJsonObject("Nested").containsKey("Birdie") 
                && modifiedJson.getJsonObject("Nested").getString("Birdie").equals("Bubbly"));
    }
}
