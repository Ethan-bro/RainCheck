package interface_adapter.createTag;

import entity.CustomTag;

import use_case.createCustomTag.cctInputBoundary;
import use_case.createCustomTag.cctInputData;

public class cctController {

    private final cctInputBoundary createCustomTagInteractor;

    public cctController(cctInputBoundary createCustomTagInteractor) {
        this.createCustomTagInteractor = createCustomTagInteractor;
    }

    public void execute(CustomTag customTag, String username) {
        cctInputData inputData = new cctInputData(customTag.getTagName(), customTag.getTagIcon());
        createCustomTagInteractor.execute(inputData, username);
    }
}
