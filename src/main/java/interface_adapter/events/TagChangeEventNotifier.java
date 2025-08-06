package interface_adapter.events;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Global event broadcaster for notifying tag updates across views/viewmodels.
 */
public class TagChangeEventNotifier {

    private TagChangeEventNotifier() {}

    private static final PropertyChangeSupport pcs = new PropertyChangeSupport(new Object());

    /**
     * Add a listener that responds to "tagsUpdated" property changes.
     */
    public static void addListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener("tagsUpdated", listener);
    }

    /**
     * Remove a listener.
     */
    public static void removeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener("tagsUpdated", listener);
    }

    /**
     * Fire the "tagsUpdated" event to all listeners.
     */
    public static void fire() {
        pcs.firePropertyChange("tagsUpdated", null, null);
    }

    /**
     * Subscribe with a Runnable (e.g., lambda) that runs when tags are updated.
     * Internally wraps the runnable as a PropertyChangeListener.
     */
    public static void subscribe(Runnable onChange) {
        PropertyChangeListener listener = evt -> {
            if ("tagsUpdated".equals(evt.getPropertyName())) {
                onChange.run();
            }
        };
        addListener(listener);
    }
}
