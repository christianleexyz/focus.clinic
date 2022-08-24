package persistence;

import model.Preset;
import model.PresetList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// NOTE: The code contained in this class has been based off the JsonWriterTest.java class
//             in the JsonSerializationDemo, found here:
//             https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Test class for JsonWriter.
public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            new PresetList();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("Expected IOException.");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyFile() {
        try {
            PresetList pl = new PresetList();
            JsonWriter writer = new JsonWriter("data/presets/testWriterEmptyFile.json");
            writer.open();
            writer.write(pl);
            writer.close();

            JsonReader reader = new JsonReader("data/presets/testWriterEmptyFile.json");
            pl = reader.read();
            assertEquals(0, pl.getPresetList().size());
        } catch (IOException e) {
            fail("Did not expect exception to be thrown.");
        }
    }

    @Test
    void testWriterStandardFile() {
        try {
            PresetList pl = new PresetList();
            pl.addPreset(new Preset("Default", 50, 10, 30));
            pl.addPreset(new Preset("Test", 1, 1, 2));
            JsonWriter writer = new JsonWriter("data/presets/testWriterStandardFile.json");
            writer.open();
            writer.write(pl);
            writer.close();

            JsonReader reader = new JsonReader("data/presets/testWriterStandardFile.json");
            pl = reader.read();
            List<Preset> presets = pl.getPresetList();
            assertEquals(2, presets.size());
            assertEquals("Default", presets.get(0).getPresetName());
            assertEquals("Test", presets.get(1).getPresetName());

        } catch (IOException e) {
            fail("Did not expect exception to be thrown.");
        }
    }
}