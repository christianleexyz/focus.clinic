package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * We use the Singleton Design Pattern to ensure that there is only
 * one EventLog in the system and that the system has global access
 * to the single instance of the EventLog.
 */

// Represents a log of preset events.
public class EventLog implements Iterable<Event> {
    /**
     * the only EventLog in the system (Singleton Design Pattern)
     */

    // Fields
    private static EventLog theLog;
    private final Collection<Event> events;

    /**
     * Prevent external construction.
     * (Singleton Design Pattern).
     */
    private EventLog() {
        events = new ArrayList<>();
    }

    /**
     * Gets instance of EventLog - creates it
     * if it doesn't already exist.
     * (Singleton Design Pattern)
     *
     * @return instance of EventLog
     */
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }
        return theLog;
    }

    /**
     * Adds an event to the event log.
     *
     * @param e the event to be added
     */
    public void logEvent(Event e) {
        events.add(e);
    }

    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}