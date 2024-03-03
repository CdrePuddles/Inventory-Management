package persistence;

import model.InventoryItem;
import model.InventoryManagement;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads inventorymanagement from JSON data stored in file
public class JsonReader {
    private String source; // TODO: review if this is correct

    // EFFECTS:     constructs reader to read from source file
    // CREDIT:      this portion is substantively modelled off of the JsonSerializationDemo
    //              provided as a reference for the term project
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS:     reads inventorymanagement from file and returns it;
    //              throws IOException if an error occurs reading data from file
    // CREDIT:      this portion is substantively modelled off of the JsonSerializationDemo
    //              provided as a reference for the term project
    public InventoryManagement read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseInventoryManagement(jsonObject);
    }

    // EFFECTS:     reads source file as string and returns it
    // CREDIT:      this portion is substantively modelled off of the JsonSerializationDemo
    //              provided as a reference for the term project
    private String readFile(String source) throws IOException {
        StringBuilder inventoryBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> inventoryBuilder.append(s));
        }

        return inventoryBuilder.toString();
    }

    // EFFECTS:     parses inventorymanagement from JSON object and returns it
    // CREDIT:      this portion is substantively modelled off of the JsonSerializationDemo
    //              provided as a reference for the term project
    private InventoryManagement parseInventoryManagement(JSONObject jsonObject) {
        // TODO: investigate this.
        String name = jsonObject.getString("name");
        InventoryManagement im = new InventoryManagement(name);
        addItems(im, jsonObject);
        return im;
    }

    // MODIFIES:    im
    // EFFECTS:     parses items from JSON object and adds them to inventorymanagement
    // CREDIT:      this portion is substantively modelled off of the JsonSerializationDemo
    //              provided as a reference for the term project
    private void addItems(InventoryManagement im, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for (Object json : jsonArray) {
            JSONObject nextItem = (JSONObject) json;
            addItem(im, nextItem);
        }
    }

    // MODIFIES:    im
    // EFFECTS:     parses item from JSON object and adds it to inventorymanagement
    // CREDIT:      this portion is substantively modelled off of the JsonSerializationDemo
    //              provided as a reference for the term project
/*
    public InventoryItem(int id, String title, int quantity, String description) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.description = description;
    } */
    private void addItem(InventoryManagement im, JSONObject jsonObject) {
        Integer id = jsonObject.getInt("id");
        String title = jsonObject.getString("title");
        Integer quantity = jsonObject.getInt("quantity");
        String description = jsonObject.getString("description");
        InventoryItem item = new InventoryItem(id, title, quantity, description);
        im.addItemJson(item);




    }
}
