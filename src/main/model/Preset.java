package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents information related to users setting custom presets.
public class Preset implements Writable {

    // Fields
    private String name;
    private long focusTime;
    private long meditationTime;
    private long restTime;

    // REQUIRES: focusTime, meditationTime, && restTime are all >= 1
    // EFFECTS: sets the name, focus time, meditation time, and rest time
    //                  from the given local parameters
    public Preset(String name, long focusTime, long meditationTime, long restTime) {
        this.name = name;
        this.focusTime = focusTime;
        this.meditationTime = meditationTime;
        this.restTime = restTime;
    }

    // REQUIRES: name is not null
    // MODIFIES: this
    // EFFECTS: sets name to this.name
    public String setPresetName(String name) {
        return this.name = name;
    }

    // REQUIRES: focusTime is a long number in type String && >= 1
    // MODIFIES: this
    // EFFECTS: sets focusTime to this.focusTime
    public long setFocusTime(String focusTime) {
        return this.focusTime = Long.parseLong(focusTime);
    }

    // REQUIRES: meditationTime is a long number in type String && >= 1
    // MODIFIES: this
    // EFFECTS: sets meditationTime to this.meditationTime
    public long setMeditationTime(String meditationTime) {
        return this.meditationTime = Long.parseLong(meditationTime);
    }

    // REQUIRES: restTime is a long number in type String && >= 1
    // MODIFIES: this
    // EFFECTS: sets restTime to this.restTime
    public long setRestTime(String restTime) {
        return this.restTime = Long.parseLong(restTime);
    }

    // EFFECTS: returns preset name
    public String getPresetName() {
        return name;
    }

    // EFFECTS: returns focusTime
    public long getFocusTime() {
        return focusTime;
    }

    // EFFECTS: returns meditationTime
    public long getMeditationTime() {
        return meditationTime;
    }

    // EFFECTS: returns restTime
    public long getRestTime() {
        return restTime;
    }

    // EFFECTS: returns focusTime in type String
    public String getFocusTimeInString() {
        return Long.toString(focusTime);
    }

    // EFFECTS: returns meditationTime in type String
    public String getMeditationTimeInString() {
        return Long.toString(meditationTime);
    }

    // EFFECTS: returns restTime in type String
    public String getRestTimeInString() {
        return Long.toString(restTime);
    }

    @Override
    // EFFECTS: saves fields to a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("focusTime", focusTime);
        json.put("meditationTime", meditationTime);
        json.put("restTime", restTime);
        return json;
    }
}