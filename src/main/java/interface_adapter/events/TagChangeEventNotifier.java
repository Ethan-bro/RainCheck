package interface_adapter.events;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Global event broadcaster for notifying tag updates across views/viewModels.
 */
public final class TagChangeEventNotifier {

    private static final String TAGS_UPDATED = "tagsUpdated";
    private static final PropertyChangeSupport PCS = new PropertyChangeSupport(new Object());

    private TagChangeEventNotifier() {

    }

    /**
     * Adds a listener that responds to the {@value #TAGS_UPDATED} property changes.
     *
     * @param listener the PropertyChangeListener to add
     */
    public static void addListener(final PropertyChangeListener listener) {
        PCS.addPropertyChangeListener(TAGS_UPDATED, listener);
    }

    /**
     * Removes a listener.
     *
     * @param listener the PropertyChangeListener to remove
     */
    public static void removeListener(final PropertyChangeListener listener) {
        PCS.removePropertyChangeListener(TAGS_UPDATED, listener);
    }

    /**
     * Fires the {@value #TAGS_UPDATED} event to all listeners.
     */
    public static void fire() {
        PCS.firePropertyChange(TAGS_UPDATED, null, null);
    }

    /**
     * Subscribes with a Runnable (e.g., lambda) that runs when tags are updated.
     * Internally wraps the runnable as a PropertyChangeListener.
     *
     * @param onChange the Runnable to run on tags update
     */
    public static void subscribe(final Runnable onChange) {
        final PropertyChangeListener listener = evt -> {
            if (TAGS_UPDATED.equals(evt.getPropertyName())) {
                onChange.run();
            }
        };
        addListener(listener);
    }
}
