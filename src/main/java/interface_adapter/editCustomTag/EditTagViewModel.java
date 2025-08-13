package interface_adapter.editCustomTag;

import entity.CustomTag;
import interface_adapter.ViewModel;

public class EditTagViewModel extends ViewModel<EditTagState> {

    private String username;


    /**
     * Constructs an EditTagViewModel with initial state and view name.
     */
    public EditTagViewModel() {
        super("Edit Custom Tag");
        setState(new EditTagState());
    }


    /**
     * Sets the username associated with this ViewModel.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Returns the username associated with this ViewModel.
     *
     * @return the current username
     */
    public String getUsername() {
        return username;
    }


    /**
     * Triggers loading of the specified tag into the view.
     * Fires a property change event named "LoadTag".
     *
     * @param tag the CustomTag to load
     */
    public void loadTag(CustomTag tag) {
        firePropertyChanged("LoadTag");
    }
}
