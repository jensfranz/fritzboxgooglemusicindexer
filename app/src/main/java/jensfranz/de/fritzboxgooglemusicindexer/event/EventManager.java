package jensfranz.de.fritzboxgooglemusicindexer.event;

import java.util.LinkedList;
import java.util.List;

public class EventManager {
    private static List<EventListener> eventListenerList;

    static {
        eventListenerList = new LinkedList<>();
    }

    public static void registerListener(final EventListener eventListener) {
        eventListenerList.add(eventListener);
    }

    public static void fireEvent(final Object event) {
        for (final EventListener eventListener : eventListenerList) {
            eventListener.eventOccured(event);
        }
    }
}
