package ui;

// Starts the application.
public class Main {

    // EFFECTS: Starts the application, and catches any exceptions made from
    //                  playing audio.

    //                  Note: Used Exception supertype to catch all exceptions since
    //                  in most cases, users will not be able to resolve this issue from the console.
    //                  Additionally, doesn't clutter the code relevant to the application with redundant
    //                  error phrases. All audio exceptions that can be thrown are
    //                  UnsupportedAudioFileException, IOException, LineUnavailableException,
    //                  SecurityException, IllegalArgumentException, and IllegalStateException.
    public static void main(String[] args) {
        try {
            new FocusClinicApp();
        } catch (Exception e) {
            System.out.println("Sorry, there was an issue running the application.");
            e.printStackTrace();
        }
    }
}