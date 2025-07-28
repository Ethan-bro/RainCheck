package use_case.createCustomTag;

import data_access.SupabaseUserDataAccessObject;
import entity.CustomTag;
import javax.swing.*;

/**
 *
 */
public class createCustomTagInteractor implements createCustomTagInputBoundary {

    private final customTagDataAccessInterface dao;
    private final createCustomTagOutputBoundary createCustomTagPresenter;

    public createCustomTagInteractor(customTagDataAccessInterface dao,
                                     createCustomTagOutputBoundary createCustomTagPresenter) {

        this.dao = dao;
        this.createCustomTagPresenter = createCustomTagPresenter;
    }

    @Override
    public void execute(createCustomTagInputData customTagInputData) {
        final String tagName = customTagInputData.getTagName();
        final ImageIcon tagIcon = customTagInputData.getTagIcon();

        // check if tag is unique. else, display fail view
        if (dao.existsByTagName(tagName)) {
            createCustomTagOutputData failedOutput = new createCustomTagOutputData(createCustomTagError.NAME_TAKEN);
            createCustomTagPresenter.prepareFailView(failedOutput);

            return;
        }

        if (dao.existsByTagIcon(tagIcon)) {
            createCustomTagOutputData failedOutput = new createCustomTagOutputData(createCustomTagError.ICON_TAKEN);
            createCustomTagPresenter.prepareFailView(failedOutput);

            return;

        } else {
            CustomTag finalTag = new CustomTag(tagName, tagIcon);
            dao.saveTag(finalTag);
            createCustomTagOutputData outputData = new createCustomTagOutputData(finalTag);
            createCustomTagPresenter.prepareSuccessView(outputData);
        }
    }
}
