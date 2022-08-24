package ui;

import model.EventLog;
import model.Preset;
import model.PresetList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// User interaction class to play and interact with the application
public class FocusClinicApp implements ActionListener {

    // Constants
    private static final String JSON_STORE = "data/presets/presets.json";
    private static final String FOCUS_NOISE_PATH = "data/sounds/focusSound_1_Minute.aif";
    private static final String MEDITATION_NOISE_PATH = "data/sounds/meditationSound_1_Minute.aif";
    private static final String REST_NOISE_PATH = "data/sounds/restSound_1_Minute.aif";
    private static final int WIDTH = 1440;
    private static final int HEIGHT = 900;
    private static final String COVER_PHOTO_PATH = "data/logos/coverPhoto.png";
    private static final String START_ICON_PATH = "data/icons/startIcon.png";
    private static final String RESUME_ICON_PATH = "data/icons/resumeIcon.png";
    private static final String PAUSE_ICON_PATH = "data/icons/pauseIcon.png";
    private static final String TIME_ICON_PATH = "data/icons/timeIcon.png";
    private static final String ADD_PRESET_ICON_PATH = "data/icons/addPresetIcon.png";
    private static final String MANAGE_PRESETS_ICON_PATH = "data/icons/managePresetsIcon.png";
    private static final String SAVE_PRESETS_ICON_PATH = "data/icons/savePresetsIcon.png";
    private static final String LOAD_PRESETS_ICON_PATH = "data/icons/loadPresetsIcon.png";
    private static final String SELECT_ICON_PATH = "data/icons/selectIcon.png";
    private static final String DELETE_ICON_PATH = "data/icons/deleteIcon.png";

    // Fields
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private Clip focusNoise;
    private Clip meditationNoise;
    private Clip restNoise;
    private ArrayList<Clip> noises;
    private PresetList presets;
    private Preset presetToPlay;
    private int currentNoiseToPlay;
    private int focusSessionsCompleted;
    private int meditationSessionsCompleted;
    private int restSessionsCompleted;
    private JFrame mainFrame;
    private JPanel homeButtonArea;
    private long timeInMicroSeconds;
    private Clip getNoiseToPlay;
    private JFrame managePresetsWindow;
    private JPanel managePresetsScrollablePanel;
    private JPanel managePresetsPanel;
    private JButton selectButton;
    private JButton deleteButton;
    private JPanel presetPanel;

    // REQUIRES: JSON_STORE to be a valid relative path
    // MODIFIES: this
    // EFFECTS: initializes sounds, graphics, exit operations, and creates new instances of a JsonWriter and JsonReader
    public FocusClinicApp() throws Exception {
        initializeSound();
        initializeGraphics();
        initializeExitOperations();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // REQUIRES: audio files to be a valid relative path
    // MODIFIES: this
    // EFFECTS: initializes sounds, puts them in a list, and creates a preset list with a default preset selected
    private void initializeSound() throws Exception {
        AudioInputStream getInputStreamForFocusNoise =
                AudioSystem.getAudioInputStream(new File(FOCUS_NOISE_PATH));
        AudioInputStream getInputStreamForMeditationNoise =
                AudioSystem.getAudioInputStream(new File(MEDITATION_NOISE_PATH));
        AudioInputStream getInputStreamForRestNoise =
                AudioSystem.getAudioInputStream(new File(REST_NOISE_PATH));

        focusNoise = AudioSystem.getClip();
        focusNoise.open(getInputStreamForFocusNoise); // gets a Clip and opens file from given path
        meditationNoise = AudioSystem.getClip();
        meditationNoise.open(getInputStreamForMeditationNoise);
        restNoise = AudioSystem.getClip();
        restNoise.open(getInputStreamForRestNoise);

        noises = new ArrayList<>();
        addNoisesInOrderOfSpecification(); // creates a list of noises to play and adds noises
        presets = new PresetList(); // creates a new PresetList and sets to default preset
        presets.addPreset(new Preset("Default", 50, 10, 30));
        presetToPlay = presets.getPresetList().get(0);
    }

    // MODIFIES: this
    // EFFECTS: sets initial field values and adds noises according to app specification
    private void addNoisesInOrderOfSpecification() {
        currentNoiseToPlay = 0; // sets first noise to play as focus noise
        focusSessionsCompleted = 0; // used for tracking sessions completed
        meditationSessionsCompleted = 0;
        restSessionsCompleted = 0;

        noises.add(focusNoise);
        noises.add(meditationNoise);
        noises.add(focusNoise);
        noises.add(meditationNoise);
        noises.add(restNoise);
    }

    // REQUIRES: COVER_PHOTO_PATH to be a valid relative path
    // MODIFIES: this
    // EFFECTS: initializes graphics component of the application
    private void initializeGraphics() {
        mainFrame = new JFrame();
        mainFrame.getContentPane().setBackground(Color.white);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.add(new JLabel(new ImageIcon(COVER_PHOTO_PATH)));

        homeButtonArea = new JPanel();
        homeButtonArea.setBackground(Color.WHITE);
        homeButtonArea.setBorder(new EmptyBorder(20, 300, 50, 300));
        homeButtonArea.setLayout(new GridLayout(2, 4));
        mainFrame.add(homeButtonArea, BorderLayout.SOUTH);
        addHomeButtons();
        mainFrame.pack();
    }

    // EFFECTS: creates and adds buttons to homeButtonArea
    private void addHomeButtons() {
        addStartButton();
        addResumeButton();
        addPauseButton();
        addTimeButton();
        addAddPresetButton();
        addManagePresetsButton();
        addSavePresetsButton();
        addLoadPresetsButton();
    }

    // REQUIRES: START_ICON_PATH to be a valid relative path
    // MODIFIES: this
    // EFFECTS: adds a start button to homeButtonArea
    private void addStartButton() {
        JButton startButton = new JButton("Start", new ImageIcon(START_ICON_PATH));
        addButtonConfiguration(startButton, "start");
    }

    // REQUIRES: RESUME_ICON_PATH to be a valid relative path
    // MODIFIES: this
    // EFFECTS: adds a resume button to homeButtonArea
    private void addResumeButton() {
        JButton resumeButton = new JButton("Resume", new ImageIcon(RESUME_ICON_PATH));
        addButtonConfiguration(resumeButton, "resume");
    }

    // REQUIRES: PAUSE_ICON_PATH to be a valid relative path
    // MODIFIES: this
    // EFFECTS: adds a pause button to homeButtonArea
    private void addPauseButton() {
        JButton pauseButton = new JButton("Pause", new ImageIcon(PAUSE_ICON_PATH));
        addButtonConfiguration(pauseButton, "pause");
    }

    // REQUIRES: TIME_ICON_PATH to be a valid relative path
    // MODIFIES: this
    // EFFECTS: adds a time button to homeButtonArea
    private void addTimeButton() {
        JButton timeButton = new JButton("Time", new ImageIcon(TIME_ICON_PATH));
        addButtonConfiguration(timeButton, "time");
    }

    // REQUIRES: ADD_PRESET_ICON_PATH to be a valid relative path
    // MODIFIES: this
    // EFFECTS: adds an add preset button to homeButtonArea
    private void addAddPresetButton() {
        JButton addPresetButton = new JButton("Add Preset", new ImageIcon(ADD_PRESET_ICON_PATH));
        addPresetButton.setIconTextGap(0);
        addPresetButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        addPresetButton.setHorizontalTextPosition(SwingConstants.CENTER);
        addPresetButton.setActionCommand("add preset");
        //addPresetButton.addActionListener(this); // comment out if adding key binding functionality

        // Line 209-218 allows for "Add Preset" to be invoked either by button click or Control + A
        AbstractAction actionForAddPresetKeyBinding = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addPreset();
            }
        };
        addPresetButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "Add Preset with Keyboard");
        addPresetButton.getActionMap().put("Add Preset with Keyboard", actionForAddPresetKeyBinding);
        addPresetButton.addActionListener(actionForAddPresetKeyBinding);
        homeButtonArea.add(addPresetButton);
    }

    // REQUIRES: MANAGE_PRESETS_ICON_PATH to be a valid relative path
    // MODIFIES: this
    // EFFECTS: adds a manage presets button to homeButtonArea
    private void addManagePresetsButton() {
        JButton managePresetsButton = new JButton("Manage Presets", new ImageIcon(MANAGE_PRESETS_ICON_PATH));
        addButtonConfiguration(managePresetsButton, "manage presets");
    }

    // REQUIRES: SAVE_PRESETS_ICON_PATH to be a valid relative path
    // MODIFIES: this
    // EFFECTS: adds a save presets button to homeButtonArea
    private void addSavePresetsButton() {
        JButton savePresetsButton = new JButton("Save Presets", new ImageIcon(SAVE_PRESETS_ICON_PATH));
        addButtonConfiguration(savePresetsButton, "save presets");
    }

    // REQUIRES: LOAD_PRESETS_ICON_PATH to be a valid relative path
    // MODIFIES: this
    // EFFECTS: adds a load presets button to homeButtonArea
    private void addLoadPresetsButton() {
        JButton loadPresetsButton = new JButton("Load Presets", new ImageIcon(LOAD_PRESETS_ICON_PATH));
        addButtonConfiguration(loadPresetsButton, "load presets");
    }

    // MODIFIES: this
    // EFFECTS: configures button placement for GUI component and adds it to homeButtonArea
    private void addButtonConfiguration(JButton button, String actionCommand) {
        button.setIconTextGap(0);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        homeButtonArea.add(button);
    }

    // MODIFIES: this
    // EFFECTS: prints an EventLog to the console before the user exits the application
    private void initializeExitOperations() {
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                printEventLog(EventLog.getInstance());
                System.exit(0);
            }
        });
    }

    // EFFECTS: prints the date and its description for every event in EventLog
    private void printEventLog(EventLog el) {
        for (model.Event next : el) {
            System.out.println(next.toString());
        }
    }

    // MODIFIES: this
    // EFFECTS: performs commands based on user action
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    // Could not further abstract it in a way that makes sense
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("start")) {
            startSession();
        }
        if (e.getActionCommand().equals("resume")) {
            resumeSession();
        }
        if (e.getActionCommand().equals("pause")) {
            pauseSession();
        }
        if (e.getActionCommand().equals("time")) {
            showTime();
        }
        if (e.getActionCommand().equals("add preset")) {
            addPreset();
        }
        if (e.getActionCommand().equals("manage presets")) {
            managePresets();
        }
        if (e.getActionCommand().equals("save presets")) {
            savePresets();
        }
        if (e.getActionCommand().equals("load presets")) {
            loadPresets();
        }
    }

    // EFFECTS: starts the session by playing noise with the selected preset
    private void startSession() {
        new Thread(() -> {
            try {
                playNoise();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).start();
    }

    // MODIFIES: this
    // EFFECTS: plays noises in a loop according to app specification and logs sessions completed
    private void playNoise() throws IOException {
        while (true) {
            playNextNoise(presetToPlay.getFocusTime());
            focusSessionsCompleted++;

            playNextNoise(presetToPlay.getMeditationTime());
            meditationSessionsCompleted++;

            playNextNoise(presetToPlay.getFocusTime());
            focusSessionsCompleted++;

            playNextNoise(presetToPlay.getMeditationTime());
            meditationSessionsCompleted++;

            playNextNoise(presetToPlay.getRestTime());
            restSessionsCompleted++;
        }
    }

    // REQUIRES: timeInMinutes >= 1
    // MODIFIES: this
    // EFFECTS: logs current system time and plays noise with currentNoiseToPlay, then increments it by 1
    private void playNextNoise(long timeInMinutes) {
        timeInMicroSeconds = convertMinutesToMicroSeconds(timeInMinutes);
        getNoiseToPlay = noises.get(currentNoiseToPlay);
        getNoiseToPlay.start();
        getNoiseToPlay.loop(Clip.LOOP_CONTINUOUSLY);
        while (getNoiseToPlay.getMicrosecondPosition() < timeInMicroSeconds) {
            // loops continuously until the noise has reached given timeInMinutes
        }
        getNoiseToPlay.stop(); // noise stops playing once it reaches timeInMinutes
        getNoiseToPlay.setMicrosecondPosition(0); // resets playback position to 0
        currentNoiseToPlay = (currentNoiseToPlay + 1) % 5; // increments to next noise to play
    }

    // REQUIRES: durationInMinutes >= 0
    //  EFFECTS: converts minutes to microseconds
    private long convertMinutesToMicroSeconds(long durationInMinutes) {
        return durationInMinutes * 60000000;
    }

    // MODIFIES: this
    // EFFECTS: resumes session after session has been paused
    private void resumeSession() {
        try {
            getNoiseToPlay.start();
            getNoiseToPlay.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (NullPointerException e) {
            // do nothing (usually occurs when user clicks resume button without starting the session)
        }
    }

    // MODIFIES: this
    // EFFECTS: pauses session after session has started or resumed
    private void pauseSession() {
        try {
            getNoiseToPlay.stop();
        } catch (NullPointerException exception) {
            // do nothing (usually occurs when user clicks pause button without starting the session)
        }
    }

    // REQUIRES: WIDTH > 0 && HEIGHT > 0
    // MODIFIES: this
    // EFFECTS: opens a new window that shows time session details
    private void showTime() {
        try {
            JFrame timeWindow = new JFrame();
            timeWindow.getContentPane().setBackground(Color.white);
            JTextArea timeDetailsText = new JTextArea();
            timeWindow.setLayout(new GridBagLayout());
            timeWindow.setMinimumSize(new Dimension(WIDTH / 2, HEIGHT / 2));
            timeWindow.setLocationRelativeTo(null);
            timeWindow.setVisible(true);
            timeDetailsText.setText(getTimeDetails());
            timeDetailsText.setEnabled(false);
            timeDetailsText.setBorder(new EmptyBorder(30, 30, 30, 30));
            timeWindow.add(timeDetailsText);
            timeWindow.pack();
        } catch (NullPointerException e) {
            // do nothing (usually occurs when user clicks time button without starting the session)
        }
    }

    // EFFECTS: displays remaining time for the current step and time spent for each session
    private String getTimeDetails() {
        return "There is "
                + ((timeInMicroSeconds - getNoiseToPlay.getMicrosecondPosition()) / 60000000)
                + isRemainingTimeOneMinute() + "and "
                + (((timeInMicroSeconds - getNoiseToPlay.getMicrosecondPosition()) % 60000000) / 1000000)
                + isRemainingTimeOneSecond() + "remaining before you will " + getNextStep()
                + "\n"
                + "\n"
                + "\n"
                + "\nTime Spent                                                            "
                + "\n"
                + "\nFocusing: "
                + (presetToPlay.getFocusTime() * focusSessionsCompleted) + " minutes "
                + "(" + focusSessionsCompleted + " x " + presetToPlay.getFocusTime() + "-minute sessions)"
                + "\nMeditating: "
                + (presetToPlay.getMeditationTime() * meditationSessionsCompleted) + " minutes "
                + "(" + meditationSessionsCompleted + " x " + presetToPlay.getMeditationTime()
                + "-minute sessions)"
                + "\nResting: "
                + (presetToPlay.getRestTime() * restSessionsCompleted) + " minutes "
                + "(" + restSessionsCompleted + " x " + presetToPlay.getRestTime() + "-minute sessions)";
    }

    // EFFECTS: determines if remaining time should display minute in singular form
    private String isRemainingTimeOneMinute() {
        if (((timeInMicroSeconds - getNoiseToPlay.getMicrosecondPosition()) / 60000000) == 1) {
            return " minute ";
        } else {
            return " minutes ";
        }
    }

    // EFFECTS: determines if remaining time should display second in singular form
    private String isRemainingTimeOneSecond() {
        if ((((timeInMicroSeconds - getNoiseToPlay.getMicrosecondPosition()) % 60000000) / 1000000) == 1) {
            return " second ";
        } else {
            return " seconds ";
        }
    }

    // EFFECTS: determines the next step to complete
    private String getNextStep() {
        switch (currentNoiseToPlay) {
            case 0: // or 2
            case 2:
                return "meditate.";
            case 1: // or 4
            case 4:
                return "focus.";
            case 3:
                return "rest.";
            default:
                return "Unable to determine next step (additional noises added).";
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new window for user to create a new preset and adds it to presets list
    private void addPreset() {
        try {
            JTextField presetName = new JTextField(10);
            JTextField focus = new JTextField(10);
            JTextField meditate = new JTextField(10);
            JTextField rest = new JTextField(10);
            JPanel addPresetPanel = new JPanel();
            addPresetPanel.setBackground(Color.white);
            addPresetPanel.add(new JLabel("Preset Name:"));
            addPresetPanel.add(presetName);
            addPresetPanel.add(new JLabel("Focus Time:"));
            addPresetPanel.add(focus);
            addPresetPanel.add(new JLabel("Meditation Time:"));
            addPresetPanel.add(meditate);
            addPresetPanel.add(new JLabel("Rest Time:"));
            addPresetPanel.add(rest);
            configureOptionPaneColor();
            JOptionPane.showConfirmDialog(null, addPresetPanel,
                    "Preset Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            Preset newPresetToAdd = new Preset(presetName.getText(), Integer.parseInt(focus.getText()),
                    Integer.parseInt(meditate.getText()), Integer.parseInt(rest.getText()));
            presets.addPreset(newPresetToAdd);
        } catch (NumberFormatException e) {
            // do nothing (usually occurs when user exits window without finishing adding a preset)
        }
    }

    // EFFECTS: sets the option pane background to white
    private void configureOptionPaneColor() {
        UIManager.put("OptionPane.background", Color.white);
        UIManager.put("Panel.background", Color.white);
    }

    // MODIFIES: this
    // EFFECTS: creates a new window that allows users to view, select, or delete their preset list
    private void managePresets() {
        managePresetsWindow = new JFrame();
        managePresetsWindow.setSize(1000, 600);
        managePresetsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        managePresetsWindow.setVisible(true);

        managePresetsScrollablePanel = new JPanel();
        managePresetsScrollablePanel.setBackground(Color.white);
        managePresetsScrollablePanel.setLayout(new BoxLayout(managePresetsScrollablePanel, BoxLayout.PAGE_AXIS));
        JScrollPane managePresetsScrollPane = new JScrollPane(managePresetsScrollablePanel);
        managePresetsScrollPane.setBackground(Color.white);
        managePresetsWindow.add(managePresetsScrollPane);
        managePresetsWindow.getContentPane().setBackground(Color.white);

        JLabel selectedPreset = new JLabel("       Selected Preset: " + presetToPlay.getPresetName() + "          ");
        selectedPreset.setBorder(new EmptyBorder(20, 20, 20, 20));
        managePresetsScrollablePanel.setBorder(new EmptyBorder(15, 10, 10, 10));
        managePresetsScrollablePanel.add(selectedPreset);
        makePresetsGUI();
    }

    // MODIFIES: this
    // EFFECTS: makes a panel with a list of presets that allows users to view, select, or delete their preset list
    private void makePresetsGUI() {
        for (Preset preset : presets.getPresetList()) {
            instantiateManagePresetsPanel();
            JTextArea presetDetails = new JTextArea(
                    "          Preset Name: " + preset.getPresetName() + "          "
                            + "\n"
                            + "\n          Focus Time: " + preset.getFocusTimeInString() + " minutes          "
                            + "\n          Meditation Time: " + preset.getMeditationTimeInString()
                            + " minutes          "
                            + "\n          Rest Time: " + preset.getRestTimeInString() + " minutes          ");

            presetDetails.setBorder(new EmptyBorder(10, 10, 2, 10));
            presetDetails.setEnabled(false);
            managePresetsPanel.add(presetDetails);
            makePresetPanel();
            selectButton.addActionListener(e -> {
                presetToPlay = preset;
                showConfirmationWindow("Preset Selected.");
            });
            deleteButton.addActionListener(e -> {
                presets.deletePreset(preset);
                showConfirmationWindow("Preset Deleted.");
            });
            constructManagePresetsWindow();
        }
    }

    // MODIFIES: this
    // EFFECTS: assigns managePresetsPanel a new JPanel and sets the layout
    private void instantiateManagePresetsPanel() {
        managePresetsPanel = new JPanel();
        managePresetsPanel.setLayout(new GridBagLayout());
    }

    // REQUIRES: SELECT_ICON_PATH && DELETE_ICON_PATH to be a valid relative path
    // MODIFIES: this
    // EFFECTS: makes a preset panel with buttons select and delete
    private void makePresetPanel() {
        presetPanel = new JPanel();
        presetPanel.setLayout(new GridLayout(2, 2));
        selectButton = new JButton("Select", new ImageIcon(SELECT_ICON_PATH));
        configurePresetPanelButton(selectButton);
        deleteButton = new JButton("Delete", new ImageIcon(DELETE_ICON_PATH));
        configurePresetPanelButton(deleteButton);
    }

    // MODIFIES: this
    // EFFECTS: configures button placement for GUI component and adds it to presetPanel
    private void configurePresetPanelButton(JButton button) {
        button.setIconTextGap(0);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setPreferredSize(new Dimension(200, 50));
        JPanel buttonPadding = new JPanel();
        buttonPadding.setLayout(new GridBagLayout());
        buttonPadding.setBorder(new EmptyBorder(5, 10, 5, 10));
        buttonPadding.setBackground(Color.white);
        buttonPadding.add(button);
        presetPanel.add(buttonPadding);
    }

    // EFFECTS: creates and shows a new popup window confirming preset action
    private void showConfirmationWindow(String confirmationText) {
        JFrame confirmationWindow = new JFrame();
        confirmationWindow.getContentPane().setBackground(Color.white);
        JTextArea presetActionConfirmationText = new JTextArea(confirmationText);
        confirmationWindow.setLayout(new GridBagLayout());
        confirmationWindow.setMinimumSize(new Dimension(350, 100));
        confirmationWindow.setLocationRelativeTo(null);
        confirmationWindow.setVisible(true);
        presetActionConfirmationText.setEnabled(false);
        presetActionConfirmationText.setBorder(new EmptyBorder(50, 125, 50, 125));
        confirmationWindow.add(presetActionConfirmationText);
        confirmationWindow.pack();
    }

    // MODIFIES: this
    // EFFECTS: finishes construction of the GUI component for the manage presets window
    private void constructManagePresetsWindow() {
        managePresetsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        managePresetsPanel.setBackground(Color.white);
        managePresetsPanel.add(presetPanel);
        managePresetsScrollablePanel.add(managePresetsPanel);
        managePresetsWindow.setLocationRelativeTo(null);
    }

    // MODIFIES: this
    // EFFECTS: saves the current session to file and opens a popup window confirming action
    private void savePresets() {
        try {
            jsonWriter.open();
            jsonWriter.write(presets);
            jsonWriter.close();
            showPersistenceConfirmationWindow("Your presets have been saved.");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads presets from file and opens a popup window confirming action
    private void loadPresets() {
        try {
            presets = jsonReader.read();
            showPersistenceConfirmationWindow("Your presets have been loaded.");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: creates and shows a new popup window confirming persistence action
    private void showPersistenceConfirmationWindow(String persistenceConfirmationText) {
        JFrame persistenceConfirmationWindow = new JFrame();
        persistenceConfirmationWindow.getContentPane().setBackground(Color.white);
        JTextArea persistenceActionConfirmationText = new JTextArea(persistenceConfirmationText);
        persistenceConfirmationWindow.setLayout(new GridBagLayout());
        persistenceConfirmationWindow.setMinimumSize(new Dimension(300, 100));
        persistenceConfirmationWindow.setLocationRelativeTo(null);
        persistenceConfirmationWindow.setVisible(true);
        persistenceActionConfirmationText.setEnabled(false);
        persistenceActionConfirmationText.setBorder(new EmptyBorder(40, 55, 40, 55));
        persistenceConfirmationWindow.add(persistenceActionConfirmationText);
        persistenceConfirmationWindow.pack();
    }
}