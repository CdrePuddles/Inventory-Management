package persistence;

import model.InventoryManagement;
import org.json.JSONObject;

import java.io.*;

// Represents a writer that writes JSON representation of Inventory Management to file
public class JsonWriter {
    private static final int TAB = 4;  // TODO: will need to review what this is for
    private PrintWriter writer;        // TODO: will need to review what this is for
    private final String destination;        // TODO: will need to review what this is for

    // EFFECTS:     constructs writer to write to the destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES:    this
    // EFFECTS:     opens writer; throws FileNOtFoundException if destination file
    //              cannot be opened for writing
    // CREDIT:      this portion is substantively modelled off of the JsonSerializationDemo
    //              provided as a reference for the term project
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES:    this
    // EFFECTS:     writes JSON representation of InventoryManagement to file
    // CREDIT:      this portion is substantively modelled off of the JsonSerializationDemo
    //              provided as a reference for the term project
    public void write(InventoryManagement im) {
        //TODO: implement toJson in InventoryManagement
        JSONObject json = im.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES:    this
    // EFFECTS:     closes writer
    // CREDIT:      this portion is substantively modelled off of the JsonSerializationDemo
    //              provided as a reference for the term project
    public void close() {
        writer.close();
    }

    // MODIFIES:    this
    // EFFECTS:     writes string to file
    // CREDIT:      this portion is substantively modelled off of the JsonSerializationDemo
    //              provided as a reference for the term project
    public void saveToFile(String json) {
        writer.print(json);
    }
}
