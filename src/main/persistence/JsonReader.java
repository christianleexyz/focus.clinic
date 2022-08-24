package persistence;

import model.Preset;
import model.PresetList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// NOTE: The code contained in this class has been based off the JsonReader.java class
//             in the JsonSerializationDemo, found here:
//             https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads relevant fields in classes from JSON data stored in file.
public class JsonReader {

    // Fields
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads presetList from file and returns it;
    // throws IOException if an error occurs reading data from file
    public PresetList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePresetList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses PresetList from JSON object and returns it
    private PresetList parsePresetList(JSONObject jsonObject) {
        PresetList pl = new PresetList();
        addPresets(pl, jsonObject);
        return pl;
    }

    // MODIFIES: pl
    // EFFECTS: parses presets from JSON object and adds them to pl
    private void addPresets(PresetList pl, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("presetList");
        for (Object json : jsonArray) {
            JSONObject nextPreset = (JSONObject) json;
            addPreset(pl, nextPreset);
        }
    }

    // MODIFIES: pl
    // EFFECTS: parses preset from JSON object and adds it to pl
    private void addPreset(PresetList pl, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        long focusTime = jsonObject.getLong("focusTime");
        long meditationTime = jsonObject.getLong("meditationTime");
        long restTime = jsonObject.getLong("restTime");
        Preset preset = new Preset(name, focusTime, meditationTime, restTime);
        pl.addPreset(preset);
    }
}