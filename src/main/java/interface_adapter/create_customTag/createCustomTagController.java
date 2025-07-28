package interface_adapter.create_customTag;

import entity.CustomTag;
import use_case.createCustomTag.createCustomTagInputBoundary;
import use_case.createCustomTag.createCustomTagInputData;

import javax.swing.*;

public class createCustomTagController {

    private final createCustomTagInputBoundary createCustomTagInteractor;

    public createCustomTagController(createCustomTagInputBoundary createCustomTagInteractor) {
        this.createCustomTagInteractor = createCustomTagInteractor;
    }

    public void execute(String customTagName, ImageIcon customTagIcon) {
        final createCustomTagInputData inputData = new createCustomTagInputData(customTagName, customTagIcon);

        createCustomTagInteractor.execute(inputData);
    }

}
