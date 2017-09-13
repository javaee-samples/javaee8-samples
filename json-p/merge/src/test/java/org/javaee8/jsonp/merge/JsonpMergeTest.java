package org.javaee8.jsonp.merge;

import javax.json.Json;
import javax.json.JsonMergePatch;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class that tests and demonstrates the JSON-P 1.1 Merge Operations.
 * @author Andrew Pielage
 */
@RunWith(Arquillian.class)
public class JsonpMergeTest {
    
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
     * Test that the JSON Merge operation replaces values as intended. 
     */
    @Test
    public void replaceTest() {
        // Create a JSON object that we'll merge into the class variable, replacing object members and array values
        JsonObject jsonToMerge = Json.createObjectBuilder()
                .add("Wibbly", "Bibbly")
                .add("Replaced", "Yes")
                .add("Lexicon", Json.createArrayBuilder()
                        .add("Wibbles")
                        .add("Bibbles")
                        .build())
                .add("Nested", Json.createObjectBuilder()
                        .add("Bestiary", Json.createArrayBuilder()
                                .add("Slyzard")
                                .add("Dragon")
                                .add("Ekimmara")
                                .build())
                        .build())
                .build();
        
        // Create a merge patch and apply it
        JsonMergePatch mergePatch = Json.createMergePatch(jsonToMerge);
        JsonValue mergedJson = mergePatch.apply(json);
        
        // Print out to more easily see what we've done
        System.out.println("JsonpMergeTest.replaceTest: Before Merge: " + json);
        System.out.println("JsonpMergeTest.replaceTest: JSON to Merge: " + jsonToMerge);
        System.out.println("JsonpMergeTest.replaceTest: After Merge: " + mergedJson);
        
        // Test that everything is as it should be
        JsonObject mergedJsonObject = mergedJson.asJsonObject();
        assertTrue("Merged JSON didn't merge correctly!", mergedJsonObject.getString("Wibbly").equals("Bibbly"));
        assertTrue("Merged JSON didn't merge correctly!", mergedJsonObject.getString("Replaced").equals("Yes"));
        assertTrue("JSON Array didn't merge correctly!", 
                mergedJsonObject.getJsonArray("Lexicon").getString(0).equals("Wibbles")
                && mergedJsonObject.getJsonArray("Lexicon").getString(1).equals("Bibbles"));
        assertTrue("Nested JSON didn't merge correctly!", 
                mergedJsonObject.getJsonObject("Nested").getString("Birdie").equals("Wordie"));
        assertTrue("Nested JSON Array didn't merge correctly!", 
                mergedJsonObject.getJsonObject("Nested").getJsonArray("Bestiary").getString(0).equals("Slyzard")
                && mergedJsonObject.getJsonObject("Nested").getJsonArray("Bestiary").getString(1).equals("Dragon") 
                && mergedJsonObject.getJsonObject("Nested").getJsonArray("Bestiary").getString(2).equals("Ekimmara"));
    }
    
    /**
     * Test that the JSON Merge operation adds values as intended. 
     */
    @Test
    public void addTest() {
        // Create a JSON object that we'll merge into the class variable, adding object members and array values
        JsonObject jsonToMerge = Json.createObjectBuilder()
                .add("Bibbly", "Bobbly")
                .add("Lexicon", Json.createArrayBuilder()
                        .add("Wibbles")
                        .add("Wobbles")
                        .add("Bibbles")
                        .add("Bobbles")
                        .build())
                .build();
        
        // Create a merge patch and apply it
        JsonMergePatch mergePatch = Json.createMergePatch(jsonToMerge);
        JsonValue mergedJson = mergePatch.apply(json);
        
        // Print out to more easily see what we've done
        System.out.println("JsonpMergeTest.addTest: Before Merge: " + json);
        System.out.println("JsonpMergeTest.addTest: JSON to Merge: " + jsonToMerge);
        System.out.println("JsonpMergeTest.addTest: After Merge: " + mergedJson);
        
        // Test that everything is as it should be
        JsonObject mergedJsonObject = mergedJson.asJsonObject();
        assertTrue("Merged JSON didn't merge correctly!", mergedJsonObject.getString("Wibbly").equals("Wobbly"));
        assertTrue("Merged JSON didn't merge correctly!", mergedJsonObject.getString("Bibbly").equals("Bobbly"));
        assertTrue("Merged JSON didn't merge correctly!", !mergedJsonObject.getBoolean("Replaced"));
        assertTrue("JSON Array didn't merge correctly!", 
                mergedJsonObject.getJsonArray("Lexicon").getString(0).equals("Wibbles")
                && mergedJsonObject.getJsonArray("Lexicon").getString(1).equals("Wobbles")
                && mergedJsonObject.getJsonArray("Lexicon").getString(2).equals("Bibbles")
                && mergedJsonObject.getJsonArray("Lexicon").getString(3).equals("Bobbles"));
        assertTrue("Nested JSON didn't merge correctly!", 
                mergedJsonObject.getJsonObject("Nested").getString("Birdie").equals("Wordie"));
        assertTrue("Nested JSON Array didn't merge correctly!", 
                mergedJsonObject.getJsonObject("Nested").getJsonArray("Bestiary").getString(0).equals("Drowner")
                && mergedJsonObject.getJsonObject("Nested").getJsonArray("Bestiary").getString(1).equals("Werewolf") 
                && mergedJsonObject.getJsonObject("Nested").getJsonArray("Bestiary").getString(2).equals("Chimera"));
    }
    
    /**
     * Test that the JSON Merge operation removes values as intended. 
     */
    @Test
    public void removeTest() {
        // Create a JSON object that we'll merge into the class variable, removing object members and array values
        JsonObject jsonToMerge = Json.createObjectBuilder()
                .addNull("Wibbly")
                .add("Lexicon", Json.createArrayBuilder()
                        .add("Wibbles")
                        .build())
                .add("Nested", Json.createObjectBuilder()
                        .addNull("Bestiary")
                        .build())
                .build();
        
        // Create a merge patch and apply it
        JsonMergePatch mergePatch = Json.createMergePatch(jsonToMerge);
        JsonValue mergedJson = mergePatch.apply(json);
        
        // Print out to more easily see what we've done
        System.out.println("JsonpMergeTest.removeTest: Before Merge: " + json);
        System.out.println("JsonpMergeTest.removeTest: JSON to Merge: " + jsonToMerge);
        System.out.println("JsonpMergeTest.removeTest: After Merge: " + mergedJson);
        
        // Test that everything is as it should be
        JsonObject mergedJsonObject = mergedJson.asJsonObject();
        assertTrue("Merged JSON didn't merge correctly!", !mergedJsonObject.containsKey("Wibbly"));
        assertTrue("Merged JSON didn't merge correctly!", !mergedJsonObject.getBoolean("Replaced"));
        assertTrue("JSON Array didn't merge correctly!", 
                mergedJsonObject.getJsonArray("Lexicon").getString(0).equals("Wibbles"));
        assertTrue("Nested JSON didn't merge correctly!", 
                mergedJsonObject.getJsonObject("Nested").getString("Birdie").equals("Wordie"));
        assertTrue("Nested JSON Array didn't merge correctly!", 
                !mergedJsonObject.getJsonObject("Nested").containsKey("Bestiary"));
    }
}
