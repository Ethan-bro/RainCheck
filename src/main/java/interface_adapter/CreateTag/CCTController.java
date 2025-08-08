package interface_adapter.CreateTag;

import entity.CustomTag;
import use_case.CreateCT.CCTInputBoundary;
import use_case.CreateCT.CCTInputData;

public class CCTController {

    private final CCTInputBoundary createCustomTagInteractor;

    public CCTController(CCTInputBoundary createCustomTagInteractor) {
        this.createCustomTagInteractor = createCustomTagInteractor;
    }

    /**
     *
     */
    public void execute(CustomTag customTag, String username) {
        CCTInputData inputData = new CCTInputData(customTag.getTagName(), customTag.getTagIcon());
        createCustomTagInteractor.execute(inputData, username);
    }
}
