package entity;

import javax.swing.*;

public class CustomTag {

    private String tagName;
    private ImageIcon tagIcon;

    public CustomTag(String tagName, ImageIcon tagIcon) {
        this.tagName = tagName;
        this.tagIcon = tagIcon;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public ImageIcon getTagIcon() {
        return tagIcon;
    }

    public void setTagIcon(ImageIcon tagIcon) {
        this.tagIcon = tagIcon;
    }
}
