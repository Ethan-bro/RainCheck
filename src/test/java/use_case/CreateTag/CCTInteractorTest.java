package use_case.CreateTag;

import data_access.InMemoryTagDataAccessObject;
import entity.CustomTag;
import interface_adapter.CreateTag.CreateTagController;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static use_case.CreateTag.TagIcons.BOOKS;
import static use_case.CreateTag.TagIcons.RING;

public class CCTInteractorTest {

    static String username = "Bobette";

    @Test
    public void testSuccessfulCreation() {

        TagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        String[] errorMsg = new String[1];

        final CreateTagOutputBoundary presenter = new CreateTagOutputBoundary() {
            @Override
            public void prepareFailView(CreateTagOutputData failedOutputData) {
                errorMsg[0] = failedOutputData.getErrorMessage();
            }

            @Override
            public void prepareSuccessView(CreateTagOutputData createCustomTagOutputData) {
                errorMsg[0] = createCustomTagOutputData.getErrorMessage();
            }
        };

        // construct new tag
        CustomTag tag = new CustomTag("school", BOOKS);
        String tagName = tag.getTagName();
        String tagIcon = tag.getTagIcon();

        final CreateTagInputBoundary interactor = new CreateTagInteractor(tagDao, presenter);
        final CreateTagController controller = new CreateTagController(interactor);

        // execute tag creation
        controller.execute(tag, username);

        // test if tagDao contains the newly created tag
        Map<String, String> tags = tagDao.getCustomTags(username);
        boolean containsTagName = tags.containsKey(tagName);
        boolean containsTagIcon = tags.get(tagName).equals(tagIcon);

        assert (containsTagName && containsTagIcon && errorMsg[0] == null);
    }

    @Test
    public void testNameTaken() {

        TagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        final String[] errorMsg = new String[1];

        final CreateTagOutputBoundary presenter = new CreateTagOutputBoundary() {
            @Override
            public void prepareFailView(CreateTagOutputData failedOutputData) {
                errorMsg[0] = failedOutputData.getErrorMessage();
            }

            @Override
            public void prepareSuccessView(CreateTagOutputData createCustomTagOutputData) {
                errorMsg[0] = createCustomTagOutputData.getErrorMessage();
            }
        };

        // construct 2 tags with the same icon
        CustomTag tagA = new CustomTag("school", BOOKS);
        CustomTag tagB = new CustomTag("school", RING);

        final CreateTagInputBoundary interactor = new CreateTagInteractor(tagDao, presenter);
        final CreateTagController controller = new CreateTagController(interactor);

        controller.execute(tagA, username);
        controller.execute(tagB, username);

        assert (errorMsg[0] == "Tag name is already in use!");
    }

}
