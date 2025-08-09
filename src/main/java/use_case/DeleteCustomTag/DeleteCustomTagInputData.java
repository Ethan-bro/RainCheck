package use_case.DeleteCustomTag;

import entity.CustomTag;

public class DeleteCustomTagInputData {

    private final String username;
    private final CustomTag tag;

    public DeleteCustomTagInputData(String username, CustomTag tag) {
        this.username = username;
        this.tag = tag;
    }

    public String getUsername() {
        return username;
    }

    public CustomTag getTag() {
        return tag;
    }
}
