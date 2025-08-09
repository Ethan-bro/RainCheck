package use_case.createCustomTag;

import data_access.InMemoryTagDataAccessObject;
import entity.CustomTag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static use_case.createCustomTag.CustomTagIcons.BOOKS;
import static use_case.createCustomTag.CustomTagIcons.RING;

public class CCTInteractorTest {

    static String username = "Bobette";

    @Test
    public void testSuccessfulCreation() {

        CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        String[] errorMsg = new String[1];

        final CCTOutputBoundary presenter = new CCTOutputBoundary() {
            @Override
            public void prepareFailView(CCTOutputData failedOutputData) {
                errorMsg[0] = failedOutputData.getErrorMessage();
            }

            @Override
            public void prepareSuccessView(CCTOutputData createCustomTagOutputData) {
                errorMsg[0] = createCustomTagOutputData.getErrorMessage();
            }
        };

        // construct new tag
        CustomTag tag = new CustomTag("school", BOOKS);
        String tagName = tag.getTagName();
        String tagIcon = tag.getTagIcon();

        final CCTInputBoundary interactor = new CCTInteractor(tagDao, presenter);
        final interface_adapter.CreateTag.CctController controller = new interface_adapter.CreateTag.CctController(interactor);

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

        CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        final String[] errorMsg = new String[1];

        final CCTOutputBoundary presenter = new CCTOutputBoundary() {
            @Override
            public void prepareFailView(CCTOutputData failedOutputData) {
                errorMsg[0] = failedOutputData.getErrorMessage();
            }

            @Override
            public void prepareSuccessView(CCTOutputData createCustomTagOutputData) {
                errorMsg[0] = createCustomTagOutputData.getErrorMessage();
            }
        };

        // construct 2 tags with the same icon
        CustomTag tagA = new CustomTag("school", BOOKS);
        CustomTag tagB = new CustomTag("school", RING);

        final CCTInputBoundary interactor = new CCTInteractor(tagDao, presenter);
        final interface_adapter.CreateTag.CctController controller = new interface_adapter.CreateTag.CctController(interactor);

        controller.execute(tagA, username);
        controller.execute(tagB, username);

        assert (errorMsg[0] == "Tag name is already in use!");
    }

}
