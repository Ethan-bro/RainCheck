package use_case.createCustomTag;

import entity.CustomTag;

import javax.swing.*;

public interface customTagDataAccessInterface {

    boolean existsByTagName(String tagName);

    boolean existsByTagIcon(ImageIcon tagIcon);

    // add user as a parameter
    void saveTag(CustomTag finalTag);
}
