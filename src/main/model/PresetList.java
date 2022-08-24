package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents information related to adding, deleting, and viewing a user's preset list.
public class PresetList implements Writable {

    // Constants
    private static final long DEFAULT_FOCUS_TIME = 50;
    private static final long DEFAULT_MEDITATION_TIME = 10;
    private static final long DEFAULT_REST_TIME = 30;

    // Fields
    private final List<Preset> presetList;

    // EFFECTS: presetList is assigned a new ArrayList instance and a default preset is added
    public PresetList() {
        presetList = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds given preset to the presetList and logs the event
    public void addPreset(Preset preset) {
        presetList.add(preset);
        logPresetEvent("Added", preset);
    }

    // REQUIRES: preset must be a valid preset that exists in this.presetList
    // MODIFIES: this
    // EFFECTS: deletes given preset from the presetList and logs the event
    public void deletePreset(Preset preset) {
        presetList.remove(preset);
        logPresetEvent("Deleted", preset);
    }

    // REQUIRES: event != null
    // MODIFIES: events in EventLog
    // EFFECTS: logs the preset event occurred
    private void logPresetEvent(String event, Preset preset) {
        EventLog.getInstance().logEvent(new Event(event + " Preset: " + preset.getPresetName() + "."));
    }

    // EFFECTS: returns the presetList
    public List<Preset> getPresetList() {
        return presetList;
    }

    // EFFECTS: returns the constant DEFAULT_FOCUS_TIME
    public long getDefaultFocusTime() {
        return DEFAULT_FOCUS_TIME;
    }

    // EFFECTS: returns the constant DEFAULT_MEDITATION_TIME
    public long getDefaultMeditationTime() {
        return DEFAULT_MEDITATION_TIME;
    }

    // EFFECTS: returns the constant DEFAULT_REST_TIME
    public long getDefaultRestTime() {
        return DEFAULT_REST_TIME;
    }

    @Override
    // EFFECTS: saves fields to a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("presetList", presetListToJson());
        return json;
    }

    // EFFECTS: returns presetList in this PresetList as a JSON array
    public JSONArray presetListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Preset preset : presetList) {
            jsonArray.put(preset.toJson());
        }
        return jsonArray;
    }
}