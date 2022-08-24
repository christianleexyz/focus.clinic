package persistence;

import model.PresetList;
import org.json.JSONObject;

import java.io.*;

// NOTE: The code contained in this class has been based off the JsonWriter.java class
//             in the JsonSerializationDemo, found here:
//             https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a writer that writes JSON representation of PresetList to file.
public class JsonWriter {

    // Constants
    private static final int TAB = 4;

    // Fields
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of PresetList to file
    public void write(PresetList pl) {
        JSONObject json = pl.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}