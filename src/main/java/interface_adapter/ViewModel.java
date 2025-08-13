package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The ViewModel for our CA implementation.
 * This class delegates work to a PropertyChangeSupport object for
 * managing the property change events.
 *
 * @param <T> The type of state object contained in the model.
 */
public class ViewModel<T> {

    private final String viewName;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private T state;

    /**
     * Constructs a ViewModel with the given view name.
     * @param viewName the name of the view
     */
    public ViewModel(String viewName) {
        this.viewName = viewName;
    }

    /**
     * Gets the name of the view.
     * @return the view name
     */
    public String getViewName() {
        return this.viewName;
    }

    /**
     * Gets the current state.
     * @return the state
     */
    public T getState() {
        return this.state;
    }

    /**
     * Sets the current state.
     * @param state the state to set
     */
    public void setState(T state) {
        this.state = state;
    }

    /**
     * Fires a property changed event for the state of this ViewModel.
     */
    public void firePropertyChanged() {
        this.support.firePropertyChange("state", null, this.state);
    }

    /**
     * Fires a property changed event for the state of this ViewModel with a custom property name.
     * @param propertyName the label for the property that was changed
     */
    public void firePropertyChanged(String propertyName) {
        this.support.firePropertyChange(propertyName, null, this.state);
    }

    /**
     * Fires a property change event with the specified property name and value change.
     * @param propertyName the name of the property that changed
     * @param oldValue the old value of the property (may be {@code null})
     * @param newValue the new value of the property (may be {@code null})
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.support.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Adds a PropertyChangeListener to this ViewModel.
     * @param listener the PropertyChangeListener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }
}
