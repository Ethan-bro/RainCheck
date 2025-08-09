package interface_adapter;

import java.util.Map;

import javax.swing.JPanel;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 * Also holds a view map: name → JPanel
 */
public class ViewManagerModel extends ViewModel<String> {

    private Map<String, JPanel> viewMap;

    /**
     * Constructs a ViewManagerModel with initial state set to empty string.
     */
    public ViewManagerModel() {
        super("view manager");
        this.setState("");
    }

    /**
     * Sets the view map from view names to their corresponding JPanel.
     *
     * @param viewMap a map associating view names with JPanels
     */
    public void setViewMap(Map<String, JPanel> viewMap) {
        this.viewMap = viewMap;
    }

    /**
     * Retrieves the JPanel associated with the given view name.
     *
     * @param viewName the name of the view to retrieve
     * @return the JPanel corresponding to the view name
     * @throws IllegalStateException if the view map has not been initialized
     */
    public JPanel getView(String viewName) {
        if (viewMap == null) {
            throw new IllegalStateException("View map not initialized. Call setViewMap first.");
        }
        return viewMap.get(viewName);
    }
}
