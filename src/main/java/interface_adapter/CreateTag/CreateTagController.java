package interface_adapter.CreateTag;

import entity.CustomTag;
import use_case.CreateTag.CreateTagInputBoundary;
import use_case.CreateTag.CreateTagInputData;

public class CreateTagController {

    private final CreateTagInputBoundary createCustomTagInteractor;

    public CreateTagController(CreateTagInputBoundary createCustomTagInteractor) {
        this.createCustomTagInteractor = createCustomTagInteractor;
    }

    /**
     *
     */
    public void execute(CustomTag customTag, String username) {
        CreateTagInputData inputData = new CreateTagInputData(customTag.getTagName(), customTag.getTagIcon());
        createCustomTagInteractor.execute(inputData, username);
    }
}
