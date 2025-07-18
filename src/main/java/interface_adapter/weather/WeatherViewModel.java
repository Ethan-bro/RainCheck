package interface_adapter.weather;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class WeatherViewModel {

    private WeatherState state = new WeatherState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public WeatherState getState() {
        return state;
    }

    public void setState(WeatherState state) {
        this.state = state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }
}
