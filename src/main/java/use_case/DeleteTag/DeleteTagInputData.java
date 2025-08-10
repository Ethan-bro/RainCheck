package use_case.DeleteTag;

import entity.CustomTag;

public class DeleteTagInputData {

    private final String username;
    private final CustomTag tag;

    public DeleteTagInputData(String username, CustomTag tag) {
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
