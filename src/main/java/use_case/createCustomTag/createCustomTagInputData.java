package use_case.createCustomTag;

import javax.swing.*;

/**
 * The input data required for creating a custom tag.
 */
public class createCustomTagInputData {

    private String tagName;
    private ImageIcon tagEmoji;

    public createCustomTagInputData(String tagName, ImageIcon tagEmoji) {
        this.tagName = tagName;
        this.tagEmoji = tagEmoji;
    }

    public String getTagName() {
        return tagName;
    }

    public ImageIcon getTagEmoji() {
        return tagEmoji;
    }

    public void setTagEmoji(ImageIcon tagEmoji) {
        this.tagEmoji = tagEmoji;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
