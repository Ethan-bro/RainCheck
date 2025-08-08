package interface_adapter.email_settings;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View model for email settings.
 */
public class EmailSettingsViewModel extends ViewModel<EmailSettingsState> {

    private static final String TITLE_LABEL = "Email Settings";

    private EmailSettingsState state = new EmailSettingsState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public EmailSettingsViewModel() {
        super("email settings");
    }

    /**
     * Returns the title label.
     * @return the title label string
     */
    public String getTitleLabel() {
        return TITLE_LABEL;
    }

    /**
     * Sets the email settings state and fires property change event.
     * @param state the new email settings state
     */
    public void setState(EmailSettingsState state) {
        final EmailSettingsState oldState = this.state;
        this.state = state;
        support.firePropertyChange("state", oldState, state);
    }

    /**
     * Gets the current email settings state.
     * @return the current state
     */
    public EmailSettingsState getState() {
        return state;
    }

    @Override
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
