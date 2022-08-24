package model;

import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test class for PresetList.
public class PresetListTest {

    // Fields
    private PresetList testPresetList1;
    private Preset testPreset1;
    private Preset testPreset2;
    private Preset testPreset3;
    private Preset testPresetDefault;
    private List<Preset> presetListForTesting1;

    @BeforeEach
    void runBefore() {
        testPresetList1 = new PresetList();
        testPreset1 = new Preset("Study", 50, 10, 30);
        testPreset2 = new Preset("Short Focus", 25, 5, 30);
        testPreset3 = new Preset("Eating", 60, 15, 5);
        testPresetDefault = new Preset("Default", testPresetList1.getDefaultFocusTime(),
                testPresetList1.getDefaultMeditationTime(), testPresetList1.getDefaultRestTime());
        presetListForTesting1 = new ArrayList<>();
    }

    @Test
    void testConstructor() {
        assertEquals(0, testPresetList1.getPresetList().size());
    }

    @Test
    void testAddPresetOnce() {
        testPresetList1.addPreset(testPreset1);

        assertTrue(testPresetList1.getPresetList().contains(testPreset1));
        assertEquals(1, testPresetList1.getPresetList().size());
    }

    @Test
    void testAddPresetMultipleTimes() {
        testPresetList1.addPreset(testPreset1);
        testPresetList1.addPreset(testPreset2);
        testPresetList1.addPreset(testPreset3);

        assertTrue(testPresetList1.getPresetList().contains(testPreset1));
        assertTrue(testPresetList1.getPresetList().contains(testPreset2));
        assertTrue(testPresetList1.getPresetList().contains(testPreset3));
        assertEquals(3, testPresetList1.getPresetList().size());
    }

    @Test
    void testAddSamePresetMultipleTimes() {
        testPresetList1.addPreset(testPreset1);
        testPresetList1.addPreset(testPreset1);
        testPresetList1.addPreset(testPreset1);
        testPresetList1.addPreset(testPreset1);

        assertTrue(testPresetList1.getPresetList().contains(testPreset1));
        assertEquals(4, testPresetList1.getPresetList().size());
    }

    @Test
    void testDeletePresetOnce() {
        testPresetList1.addPreset(testPreset1);
        assertEquals(1, testPresetList1.getPresetList().size());
        testPresetList1.deletePreset(testPreset1);
        assertEquals(0, testPresetList1.getPresetList().size());
        assertFalse(testPresetList1.getPresetList().contains(testPreset1));
    }

    @Test
    void testDeleteMultiplePresets() {
        testPresetList1.addPreset(testPreset1);
        testPresetList1.addPreset(testPreset2);
        testPresetList1.addPreset(testPreset3);
        assertEquals(3, testPresetList1.getPresetList().size());

        testPresetList1.deletePreset(testPreset1);
        testPresetList1.deletePreset(testPreset2);
        testPresetList1.deletePreset(testPreset3);
        assertEquals(0, testPresetList1.getPresetList().size());
        assertFalse(testPresetList1.getPresetList().contains(testPreset1));
        assertFalse(testPresetList1.getPresetList().contains(testPreset2));
        assertFalse(testPresetList1.getPresetList().contains(testPreset3));
    }

    @Test
    void testDeleteSamePresetsMultipleTimes() {
        testPresetList1.addPreset(testPreset1);
        testPresetList1.addPreset(testPreset1);
        testPresetList1.addPreset(testPreset1);
        assertEquals(3, testPresetList1.getPresetList().size());

        testPresetList1.deletePreset(testPreset1);
        testPresetList1.deletePreset(testPreset1);
        testPresetList1.deletePreset(testPreset1);
        assertEquals(0, testPresetList1.getPresetList().size());
        assertFalse(testPresetList1.getPresetList().contains(testPreset1));
    }

    @Test
    void testGetPresetList() {
        presetListForTesting1.add(testPresetDefault);
        testPresetList1.addPreset(testPresetDefault);
        String expectedT1 = presetListForTesting1.get(0).getPresetName();
        String actualT1 = testPresetList1.getPresetList().get(0).getPresetName();
        assertEquals(expectedT1, actualT1);
    }

    @Test
    void testGetDefaultFocusTime() {
        assertEquals(50, testPresetList1.getDefaultFocusTime());
    } // this is testing a simple getter of a constant in the PresetList class created to help test this class
    // as such, if the constant changes, this test will fail

    @Test
    void testGetDefaultMeditationTime() {
        assertEquals(10, testPresetList1.getDefaultMeditationTime());
    } // this is testing a simple getter of a constant in the PresetList class created to help test this class
    // as such, if the constant changes, this test will fail

    @Test
    void testGetDefaultRestTime() {
        assertEquals(30, testPresetList1.getDefaultRestTime());
    } // this is testing a simple getter of a constant in the PresetList class created to help test this class
    // as such, if the constant changes, this test will fail

    @Test
    void testToJson() {
        testPresetList1.addPreset(testPreset1);
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testPreset1.toJson());
        assertEquals(testJsonArray.length(), testPresetList1.toJson().length());
    }

    @Test
    void testPresetListToJson() {
        testPresetList1.addPreset(testPreset1);
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testPreset1.toJson());
        assertEquals(testJsonArray.length(), testPresetList1.presetListToJson().length());
    }
}