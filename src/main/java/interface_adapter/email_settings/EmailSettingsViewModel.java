package interface_adapter.email_settings;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View model for email settings.
 */
public class EmailSettingsViewModel extends ViewModel {

    public final String TITLE_LABEL = "Email Settings";

    private EmailSettingsState state = new EmailSettingsState();
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public EmailSettingsViewModel() {
        super("email settings");
    }

    public void setState(EmailSettingsState state) {
        EmailSettingsState oldState = this.state;
        this.state = state;
        support.firePropertyChange("state", oldState, state);
    }

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
