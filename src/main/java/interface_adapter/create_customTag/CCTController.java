package interface_adapter.create_customTag;

import entity.CustomTag;
import use_case.createCustomTag.CCTInputBoundary;
import use_case.createCustomTag.CCTInputData;

public class CCTController {

    private final CCTInputBoundary createCustomTagInteractor;

    public CCTController(CCTInputBoundary createCustomTagInteractor) {
        this.createCustomTagInteractor = createCustomTagInteractor;
    }

    /**
     *
     */
    public void execute(CustomTag customTag, String username) {
        System.out.println("Controller received request to create tag. Username: " + username);
        CCTInputData inputData = new CCTInputData(customTag.getTagName(), customTag.getTagEmoji());
        createCustomTagInteractor.execute(inputData, username);
    }
}
