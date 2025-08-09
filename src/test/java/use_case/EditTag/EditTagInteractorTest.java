package use_case.EditTag;

import data_access.InMemoryTagDataAccessObject;
import entity.CustomTag;
import interface_adapter.editTag.EditTagController;
import interface_adapter.manageTags.ManageTagsViewModel;
import org.junit.jupiter.api.Test;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.createCustomTag.CustomTagIcons;
import use_case.edit_custom_tag.EditTagInputBoundary;
import use_case.edit_custom_tag.EditTagInteractor;
import use_case.edit_custom_tag.EditTagOutputBoundary;
import use_case.edit_custom_tag.EditTagOutputData;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EditTagInteractorTest {

    private final String username = "Barbara";

    @Test
    void testSuccessfulEdit() {
        final CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        final ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        final String[] errorMsg = new String[1];

        final EditTagOutputBoundary presenter = new EditTagOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTagOutputData successOutput) {
                errorMsg[0] = successOutput.getErrorMessage();
            }

            @Override
            public void prepareFailView(EditTagOutputData failedOutput) {
                errorMsg[0] = failedOutput.getErrorMessage();
            }
        };

        final EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter);
        final EditTagController controller = new EditTagController(tagDao, manageTagsViewModel, interactor);

        final CustomTag oldTag = new CustomTag("home", CustomTagIcons.HOUSE);
        final CustomTag newTag = new CustomTag("gym", CustomTagIcons.MUSCLE);

        controller.execute(oldTag, newTag);

        final Map<String, String> tags = tagDao.getCustomTags(username);

        assertTrue(tags.containsKey("gym"), "New tag name 'gym' should be present");
        assertEquals(CustomTagIcons.MUSCLE, tags.get("gym"), "New tag icon should be MUSCLE");
        assertFalse(tags.containsKey("home"), "Old tag name 'home' should be removed");
        assertFalse(tags.containsValue(CustomTagIcons.HOUSE), "Old tag icon HOUSE should be removed");
        assertNull(errorMsg[0], "No error message expected on successful edit");
    }

    @Test
    void testSameNameDiffIcon() {
        final CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        final ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        final String[] errorMsg = new String[1];

        final EditTagOutputBoundary presenter = new EditTagOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTagOutputData successOutput) {
                errorMsg[0] = successOutput.getErrorMessage();
            }

            @Override
            public void prepareFailView(EditTagOutputData failedOutput) {
                errorMsg[0] = failedOutput.getErrorMessage();
            }
        };

        final EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter);
        final EditTagController controller = new EditTagController(tagDao, manageTagsViewModel, interactor);

        final CustomTag oldTag = new CustomTag("home", CustomTagIcons.HOUSE);
        final CustomTag newTag = new CustomTag("home", CustomTagIcons.MUSCLE);

        controller.execute(oldTag, newTag);

        final Map<String, String> tags = tagDao.getCustomTags(username);

        assertTrue(tags.containsKey("home"), "Tag name 'home' should exist");
        assertEquals(CustomTagIcons.MUSCLE, tags.get("home"), "Tag icon should be updated to MUSCLE");
    }

    @Test
    void testSameIconDiffName() {
        final CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        final ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        final String[] errorMsg = new String[1];

        final EditTagOutputBoundary presenter = new EditTagOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTagOutputData successOutput) {
                errorMsg[0] = successOutput.getErrorMessage();
            }

            @Override
            public void prepareFailView(EditTagOutputData failedOutput) {
                errorMsg[0] = failedOutput.getErrorMessage();
            }
        };

        final EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter);
        final EditTagController controller = new EditTagController(tagDao, manageTagsViewModel, interactor);

        final CustomTag oldTag = new CustomTag("home", CustomTagIcons.HOUSE);
        tagDao.addCustomTag(username, oldTag);
        final CustomTag newTag = new CustomTag("house", CustomTagIcons.HOUSE);

        controller.execute(oldTag, newTag);

        final Map<String, String> tags = tagDao.getCustomTags(username);

        assertFalse(tags.containsKey("home"), "Old tag name 'home' should be removed");
        assertTrue(tags.containsKey("house"), "New tag name 'house' should exist");
        assertEquals(CustomTagIcons.HOUSE, tags.get("house"), "Tag icon should remain HOUSE");
    }

    @Test
    void testEditTakenName() {
        final CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        final ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        final String[] errorMsg = new String[1];

        final EditTagOutputBoundary presenter = new EditTagOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTagOutputData successOutput) {
                errorMsg[0] = successOutput.getErrorMessage();
            }

            @Override
            public void prepareFailView(EditTagOutputData failedOutput) {
                errorMsg[0] = failedOutput.getErrorMessage();
            }
        };

        final EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter);
        final EditTagController controller = new EditTagController(tagDao, manageTagsViewModel, interactor);

        final CustomTag tagA = new CustomTag("home", CustomTagIcons.HOUSE);
        tagDao.addCustomTag(username, tagA);

        final CustomTag tagB = new CustomTag("gym", CustomTagIcons.MUSCLE);
        tagDao.addCustomTag(username, tagB);

        final CustomTag tagC = new CustomTag("home", CustomTagIcons.FOOD);

        controller.execute(tagB, tagC);

        assertEquals("Tag name is taken", errorMsg[0], "Should show error for taken tag name");
    }

    @Test
    void testEditTakenIcon() {
        final CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        final ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        final String[] errorMsg = new String[1];

        final EditTagOutputBoundary presenter = new EditTagOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTagOutputData successOutput) {
                errorMsg[0] = successOutput.getErrorMessage();
            }

            @Override
            public void prepareFailView(EditTagOutputData failedOutput) {
                errorMsg[0] = failedOutput.getErrorMessage();
            }
        };

        final EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter);
        final EditTagController controller = new EditTagController(tagDao, manageTagsViewModel, interactor);

        final CustomTag tagA = new CustomTag("home", CustomTagIcons.HOUSE);
        tagDao.addCustomTag(username, tagA);

        final CustomTag tagB = new CustomTag("gym", CustomTagIcons.MUSCLE);
        tagDao.addCustomTag(username, tagB);

        final CustomTag tagC = new CustomTag("house", CustomTagIcons.HOUSE);

        controller.execute(tagB, tagC);

        assertEquals("Tag icon is taken", errorMsg[0], "Should show error for taken tag icon");
    }
}
