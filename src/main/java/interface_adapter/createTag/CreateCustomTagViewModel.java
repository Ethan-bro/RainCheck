package interface_adapter.createTag;

import interface_adapter.ViewModel;

public class CreateCustomTagViewModel extends ViewModel<CreateCustomTagState> {

    private String username;

    /**
     * Constructs a CreateCustomTagViewModel, initializing the view model with a default state.
     */
    public CreateCustomTagViewModel() {
        super("Create Custom Tag");
        setState(new CreateCustomTagState());
    }

    /**
     * Sets the username associated with this view model.
     * 
     * @param username the username to associate with this view model
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the username associated with this view model.
     *
     * @return the username currently set in this view model
     */
    public String getUsername() {
        return username;
    }

}
