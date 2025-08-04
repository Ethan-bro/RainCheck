package interface_adapter.create_customTag;

import entity.CustomTag;
import use_case.createCustomTag.CCTInputBoundary;
import use_case.createCustomTag.CCTInputData;
import view.CCTView;

public class CCTController {

    private final CCTInputBoundary createCustomTagInteractor;
    private final CCTView cctView;

    public CCTController(
            CCTInputBoundary createCustomTagInteractor,
            CCTView cctView) {
        this.createCustomTagInteractor = createCustomTagInteractor;
        this.cctView = cctView;
    }

    public void showView() {
        cctView.show();
    }

    /**
     *
     */
    public void execute(CustomTag customTag, String username) {
        CCTInputData inputData = new CCTInputData(customTag.getTagName(), customTag.getTagEmoji());
        createCustomTagInteractor.execute(inputData, username);
    }


    public CCTView getView() {
        return cctView;
    }
}
