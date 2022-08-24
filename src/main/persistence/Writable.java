package persistence;

import org.json.JSONObject;

// Represents an interface that allows classes to possess data persistence.
public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}