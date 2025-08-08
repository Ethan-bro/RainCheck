package use_case.DeleteCT;

import entity.CustomTag;

public class DeleteCTInputData {

    private final String username;
    private final CustomTag tag;

    public DeleteCTInputData(String username, CustomTag tag) {
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
