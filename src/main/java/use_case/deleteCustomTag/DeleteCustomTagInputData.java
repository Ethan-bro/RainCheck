package use_case.deleteCustomTag;

import entity.CustomTag;

public class DeleteCustomTagInputData {

    private final String username;
    private final CustomTag tag;

    /**
     * Constructs a DeleteCustomTagInputData object.
     *
     * @param username the username associated with the tag deletion
     * @param tag the custom tag to be deleted
     */
    public DeleteCustomTagInputData(String username, CustomTag tag) {
        this.username = username;
        this.tag = tag;
    }

    /**
     * Gets the username associated with the tag deletion request.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the custom tag to be deleted.
     *
     * @return the custom tag
     */
    public CustomTag getTag() {
        return tag;
    }
}
