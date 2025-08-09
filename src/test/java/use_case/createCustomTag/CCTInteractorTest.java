package use_case.createCustomTag;

import static use_case.createCustomTag.CustomTagIcons.BOOKS;
import static use_case.createCustomTag.CustomTagIcons.RING;
import static org.junit.jupiter.api.Assertions.*;

import data_access.InMemoryTagDataAccessObject;

import entity.CustomTag;

import java.util.Map;

import interface_adapter.createTag.CreateCustomTagController;
import org.junit.jupiter.api.Test;

class CCTInteractorTest {

    private static final String username = "Bobette";

    @Test
    void testSuccessfulCreation() {
        final CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        final String[] errorMsg = new String[1];

        final CreateCustomTagOutputBoundary presenter = new CreateCustomTagOutputBoundary() {
            @Override
            public void prepareFailView(CreateCustomTagOutputData failedOutputData) {
                errorMsg[0] = failedOutputData.getErrorMessage();
            }

            @Override
            public void prepareSuccessView(CreateCustomTagOutputData createCustomTagOutputData) {
                errorMsg[0] = createCustomTagOutputData.getErrorMessage();
            }
        };

        final CustomTag tag = new CustomTag("school", BOOKS);
        final String tagName = tag.getTagName();
        final String tagIcon = tag.getTagIcon();

        final CreateCustomTagInputBoundary interactor = new CreateCustomTagInteractor(tagDao, presenter);
        final CreateCustomTagController controller = new CreateCustomTagController(interactor);

        controller.execute(tag, username);

        final Map<String, String> tags = tagDao.getCustomTags(username);

        assertTrue(tags.containsKey(tagName), "Tag name should be present in tagDao");
        assertEquals(tagIcon, tags.get(tagName), "Tag icon should match the created tag");
        assertNull(errorMsg[0], "No error message expected on successful creation");
    }

    @Test
    void testNameTaken() {
        final CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        final String[] errorMsg = new String[1];

        final CreateCustomTagOutputBoundary presenter = new CreateCustomTagOutputBoundary() {
            @Override
            public void prepareFailView(CreateCustomTagOutputData failedOutputData) {
                errorMsg[0] = failedOutputData.getErrorMessage();
            }

            @Override
            public void prepareSuccessView(CreateCustomTagOutputData createCustomTagOutputData) {
                errorMsg[0] = createCustomTagOutputData.getErrorMessage();
            }
        };

        final CustomTag tagA = new CustomTag("school", BOOKS);
        final CustomTag tagB = new CustomTag("school", RING);

        final CreateCustomTagInputBoundary interactor = new CreateCustomTagInteractor(tagDao, presenter);
        final CreateCustomTagController controller = new CreateCustomTagController(interactor);

        controller.execute(tagA, username);
        controller.execute(tagB, username);

        assertEquals("Tag name is already in use!", errorMsg[0], "Expected error message for taken tag name");
    }
}
