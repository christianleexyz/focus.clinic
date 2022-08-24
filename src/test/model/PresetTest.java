package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Test class for Preset.
public class PresetTest {

    // Fields
    private Preset testPreset1;
    private Preset testPreset2;
    private Preset testPreset3;

    @BeforeEach
    void runBefore() {
        testPreset1 = new Preset("Study", 50, 10, 30);
        testPreset2 = new Preset("Short Focus", 25, 5, 30);
        testPreset3 = new Preset("Eating", 60, 15, 5);
    }

    @Test
    void testConstructor() {
        assertEquals("Study", testPreset1.getPresetName());
        assertEquals(50, testPreset1.getFocusTime());
        assertEquals(10, testPreset1.getMeditationTime());
        assertEquals(30, testPreset1.getRestTime());

        assertEquals("Short Focus", testPreset2.getPresetName());
        assertEquals(25, testPreset2.getFocusTime());
        assertEquals(5, testPreset2.getMeditationTime());
        assertEquals(30, testPreset2.getRestTime());
    }

    @Test
    void testSetPresetName() {
        testPreset1.setPresetName("Computer Science");
        testPreset2.setPresetName("Short Tasks");
        testPreset3.setPresetName("Nutrition");

        assertEquals("Computer Science", testPreset1.getPresetName());
        assertEquals("Short Tasks", testPreset2.getPresetName());
        assertEquals("Nutrition", testPreset3.getPresetName());
    }

    @Test
    void testSetFocusTime() {
        testPreset1.setFocusTime("1");
        testPreset2.setFocusTime("42");
        testPreset3.setFocusTime("10000000");

        assertEquals(1, testPreset1.getFocusTime());
        assertEquals(42, testPreset2.getFocusTime());
        assertEquals(10000000, testPreset3.getFocusTime());
    }

    @Test
    void testSetMeditationTime() {
        testPreset1.setMeditationTime("1");
        testPreset2.setMeditationTime("75");
        testPreset3.setMeditationTime("10000000");

        assertEquals(1, testPreset1.getMeditationTime());
        assertEquals(75, testPreset2.getMeditationTime());
        assertEquals(10000000, testPreset3.getMeditationTime());
    }

    @Test
    void testSetRestTime() {
        testPreset1.setRestTime("1");
        testPreset2.setRestTime("92");
        testPreset3.setRestTime("10000000");

        assertEquals(1, testPreset1.getRestTime());
        assertEquals(92, testPreset2.getRestTime());
        assertEquals(10000000, testPreset3.getRestTime());
    }

    @Test
    void testGetPresetName() {
        assertEquals("Study", testPreset1.getPresetName());
        assertEquals("Short Focus", testPreset2.getPresetName());
        assertEquals("Eating", testPreset3.getPresetName());
    }

    @Test
    void testGetFocusTime() {
        assertEquals(50, testPreset1.getFocusTime());
        assertEquals(25, testPreset2.getFocusTime());
        assertEquals(60, testPreset3.getFocusTime());
    }

    @Test
    void testGetMeditationTime() {
        assertEquals(10, testPreset1.getMeditationTime());
        assertEquals(5, testPreset2.getMeditationTime());
        assertEquals(15, testPreset3.getMeditationTime());
    }

    @Test
    void testGetRestTime() {
        assertEquals(30, testPreset1.getRestTime());
        assertEquals(30, testPreset2.getRestTime());
        assertEquals(5, testPreset3.getRestTime());
    }

    @Test
    void testGetFocusTimeInString() {
        assertEquals("50", testPreset1.getFocusTimeInString());
        assertEquals("25", testPreset2.getFocusTimeInString());
        assertEquals("60", testPreset3.getFocusTimeInString());
    }

    @Test
    void testGetMeditationTimeInString() {
        assertEquals("10", testPreset1.getMeditationTimeInString());
        assertEquals("5", testPreset2.getMeditationTimeInString());
        assertEquals("15", testPreset3.getMeditationTimeInString());
    }

    @Test
    void testGetRestTimeInString() {
        assertEquals("30", testPreset1.getRestTimeInString());
        assertEquals("30", testPreset2.getRestTimeInString());
        assertEquals("5", testPreset3.getRestTimeInString());
    }
}