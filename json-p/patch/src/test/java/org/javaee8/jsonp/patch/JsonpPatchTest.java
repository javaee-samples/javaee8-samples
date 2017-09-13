package org.javaee8.jsonp.patch;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class that tests and demonstrates the JSON-P 1.1 Patch Operations.
 * @author Andrew Pielage
 */
@RunWith(Arquillian.class)
public class JsonpPatchTest {

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
     * Test that the JSON Patch add operation works as intended. 
     */
    @Test
    public void addTest() {     
        // Create a patch that adds some object members, replaces the value of an already existing object member, and 
        // adds some extra elements to an array
        JsonPatch patch = Json.createPatchBuilder()
                .add("/Timey", "Wimey") 
                .add("/Replaced", true)
                .add("/FunnyReference", true)
                .add("/FunScore", 100)
                .add("/Lexicon/2", "Toddles")
                .add("/Lexicon/2", "Tiddles")
                .build();
        
        // Apply the patch
        JsonObject patchedJson = patch.apply(json);
        
        // Print out to more easily see what we've done
        System.out.println("JsonpPatchTest.addTest: Before Patch: " + json);
        System.out.println("JsonpPatchTest.addTest: After Patch: " + patchedJson);
        
        // Test that everything is as it should be
        assertTrue("Patched JSON doesn't match!", patchedJson.getString("Wibbly").equals("Wobbly"));
        assertTrue("Patched JSON doesn't match!", patchedJson.getString("Timey").equals("Wimey"));
        assertTrue("Patched JSON doesn't match!", patchedJson.getBoolean("FunnyReference"));
        assertTrue("Patched JSON doesn't match!", patchedJson.getInt("FunScore") == 100);
        assertTrue("Patched JSON doesn't match!", patchedJson.getBoolean("Replaced"));
        assertTrue("Patched JSON doesn't match!", patchedJson.getJsonArray("Lexicon").getString(0)
                .equals("Wibbles"));
        assertTrue("Patched JSON doesn't match!", patchedJson.getJsonArray("Lexicon").getString(1)
                .equals("Wobbles"));
        assertTrue("Patched JSON doesn't match!", patchedJson.getJsonArray("Lexicon").getString(2)
                .equals("Tiddles"));
        assertTrue("Patched JSON doesn't match!", patchedJson.getJsonArray("Lexicon").getString(3)
                .equals("Toddles"));
    }
    
    /**
     * Test that the JSON Patch remove operation works as intended. 
     */
    @Test
    public void removeTest() {
        // Create a patch that removes an object member and an array element
        JsonPatch patch = Json.createPatchBuilder()
                .remove("/Replaced")
                .remove("/Lexicon/1")
                .build();
        
        // Apply the patch
        JsonObject patchedJson = patch.apply(json);
        
        // Print out to more easily see what we've done
        System.out.println("JsonpPatchTest.removeTest: Before Patch: " + json);
        System.out.println("JsonpPatchTest.removeTest: After Patch: " + patchedJson);
        
        assertTrue("Patched JSON still contains object member!", !patchedJson.containsKey("Replaced"));
        assertTrue("Patched JSON still contains object member!", ((patchedJson.getJsonArray("Lexicon").size() == 1) 
                && (patchedJson.getJsonArray("Lexicon").getString(0).equals("Wibbles"))));
    }
    
    /**
     * Test that the JSON Patch replace operation works as intended.
     */
    @Test
    public void replaceTest() {
        // Create a patch that replaces an object member and an array element
        JsonPatch patch = Json.createPatchBuilder()
                .replace("/Replaced", true)
                .replace("/Lexicon/0", "Tiddles")
                .replace("/Lexicon/1", "Toddles")
                .build();
        
        // Apply the patch
        JsonObject patchedJson = patch.apply(json);
        
        // Print out to more easily see what we've done
        System.out.println("JsonpPatchTest.replaceTest: Before Patch: " + json);
        System.out.println("JsonpPatchTest.replaceTest: After Patch: " + patchedJson);
        
        assertTrue("Patched JSON still contains original value!", patchedJson.getBoolean("Replaced"));
        assertTrue("Patched JSON still contains original values!", 
                ((patchedJson.getJsonArray("Lexicon").getString(0).equals("Tiddles")) 
                        && (patchedJson.getJsonArray("Lexicon").getString(1).equals("Toddles"))));
    }
    
    /**
     * Test that the JSON Patch move operation works as intended.
     */
    @Test
    public void moveTest() {
        // Create a patch that moves an object member, moves an array element, and reorders an array
        JsonPatch patch = Json.createPatchBuilder()
                .move("/Nested/Tibbly", "/Wibbly")
                .move("/Nested/Bestiary/2", "/Lexicon/1")
                .move("/Nested/Bestiary/3", "/Nested/Bestiary/0")
                .build();
        
        // Apply the patch
        JsonObject patchedJson = patch.apply(json);
        
        // Print out to more easily see what we've done
        System.out.println("JsonpPatchTest.moveTest: Before Patch: " + json);
        System.out.println("JsonpPatchTest.moveTest: After Patch: " + patchedJson);
        
        assertTrue("Patched JSON hasn't moved value!", (!patchedJson.containsKey("Wibbly") 
                && patchedJson.getJsonObject("Nested").getString("Tibbly").equals("Wobbly")));
        assertTrue("Patched JSON hasn't moved value!", ((patchedJson.getJsonArray("Lexicon").size() == 1)) 
                && (patchedJson.getJsonObject("Nested").getJsonArray("Bestiary").getString(2).equals("Chimera"))
                && (patchedJson.getJsonObject("Nested").getJsonArray("Bestiary").getString(0).equals("Werewolf")));
    }
    
    /**
     * Test that the JSON Patch copy operation works as intended.
     */
    @Test
    public void copyTest() {
        // Create a patch that copies an object member and an array element
        JsonPatch patch = Json.createPatchBuilder()
                .copy("/Nested/Tobbly", "/Wibbly")
                .copy("/Nested/Bestiary/2", "/Lexicon/0")
                .build();
        
        // Apply the patch
        JsonObject patchedJson = patch.apply(json);
        
        // Print out to more easily see what we've done
        System.out.println("JsonpPatchTest.copyTest: Before Patch: " + json);
        System.out.println("JsonpPatchTest.copyTest: After Patch: " + patchedJson);
        
        assertTrue("Patched JSON hasn't moved value!", (patchedJson.containsKey("Wibbly") 
                && patchedJson.getJsonObject("Nested").getString("Tobbly").equals("Wobbly")));
        assertTrue("Patched JSON hasn't moved value!", ((patchedJson.getJsonArray("Lexicon").size() == 2)) 
                        && (patchedJson.getJsonObject("Nested").getJsonArray("Bestiary").getString(2).equals("Wibbles")));
    }
    
    /**
     * Test that the JSON Patch test operation works as intended.
     */
    @Test
    public void testTest() {
        // Create a patch that should test positive
        JsonPatch patch = Json.createPatchBuilder()
                .test("/Wibbly", "Wobbly")
                .test("/Lexicon/0", "Wibbles")
                .build();
        
        // Apply the patch
        JsonObject patchedJson = patch.apply(json);
        
        // Create a patch that should fail
        patch = Json.createPatchBuilder()
                .test("/Wibbly", "Tobbly")
                .build();
        
        try {
            patchedJson = patch.apply(json);
        } catch (JsonException ex) {
            return;
        }
        
        Assert.fail("Should have caught a JsonException and exited!"); 
    }
    
    // TODO: Ignore unrecognised elements; Adding to a non-existent target; Adding a nested member object; Error Handling
}
