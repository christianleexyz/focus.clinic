package persistence;

import model.Preset;
import model.PresetList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// NOTE: The code contained in this class has been based off the JsonReaderTest.java class
//             in the JsonSerializationDemo, found here:
//             https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Test class for JsonReader.
public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("data/NonExistentFile.json");
        try {
            reader.read();
            fail("Expected IOException.");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyFile() {
        JsonReader reader = new JsonReader("data/presets/testReaderEmptyFile.json");
        try {
            PresetList pl = reader.read();
            assertEquals(0, pl.getPresetList().size());
        } catch (IOException e) {
            fail("Error reading the file.");
        }
    }

    @Test
    void testReaderStandardFile() {
        JsonReader reader = new JsonReader("data/presets/testReaderStandardFile.json");
        try {
            PresetList pl = reader.read();
            List<Preset> presets = pl.getPresetList();
            assertEquals(2, presets.size());
            assertEquals("Default", presets.get(0).getPresetName());
            assertEquals("Test", presets.get(1).getPresetName());
        } catch (IOException e) {
            fail("Error reading the file.");
        }
    }
}