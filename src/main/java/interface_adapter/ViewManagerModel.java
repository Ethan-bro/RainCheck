package interface_adapter;

import javax.swing.*;
import java.util.Map;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 * Also holds a view map: name → JPanel
 */
public class ViewManagerModel extends ViewModel<String> {

    private Map<String, JPanel> viewMap;

    public ViewManagerModel() {
        super("view manager");
        this.setState("");
    }

    public void setViewMap(Map<String, JPanel> viewMap) {
        this.viewMap = viewMap;
    }

    public void addToViewMap(String name, JPanel view) {
        this.viewMap.put(name, view);
    }

    public JPanel getView(String viewName) {
        if (viewMap == null) {
            throw new IllegalStateException("View map not initialized. Call setViewMap first.");
        }
        return viewMap.get(viewName);
    }
}
