package use_case.EditTag;

import data_access.InMemoryTagDataAccessObject;
import entity.CustomTag;
import interface_adapter.EditTag.EditTagController;
import interface_adapter.ManageTags.ManageTagsViewModel;
import org.junit.jupiter.api.Test;
import use_case.CreateTag.TagDataAccessInterface;
import use_case.EditTag.TagReplacement.DeleteAndCreate;

import java.util.Map;

import static use_case.CreateTag.TagIcons.*;

public class EditTagInteractorTest {

    private final String username = "Barbara";

    @Test
    public void testSuccessfulEdit () {

        TagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        String[] errorMsg = new String[1];

        // create the interactor
        EditTagOutputBoundary presenter = new EditTagOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTagOutputData successOutput) {
                errorMsg[0] = successOutput.getErrorMessage();
            }

            @Override
            public void prepareFailView(EditTagOutputData failedOutput) {
                errorMsg[0] = failedOutput.getErrorMessage();
            }
        };

        EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter, new DeleteAndCreate());
        EditTagController controller = new EditTagController(tagDao, manageTagsViewModel, interactor);

        // create tag to edit
        CustomTag oldTag = new CustomTag("home", HOUSE);
        CustomTag newTag = new CustomTag("gym", MUSCLE);

        controller.execute(oldTag, newTag);

        // fetch user's tags
        Map<String, String> tags = tagDao.getCustomTags(username);

        // check if newtag was registered
        boolean containsNewName = tags.containsKey("gym");
        boolean containsNewIcon = tags.get("gym").equals(MUSCLE);

        // check if old tag was deleted
        boolean notContainsOldName = !tags.containsKey("home");
        boolean notContainsOldIcon = !tags.containsValue(HOUSE);

        assert (containsNewName && containsNewIcon && notContainsOldName && notContainsOldIcon && errorMsg[0] == null);

    }

    @Test
    public void testSameNameDiffIcon () {
        TagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        String[] errorMsg = new String[1];

        EditTagOutputBoundary presenter = new EditTagOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTagOutputData successOutput) {
                errorMsg[0] = successOutput.getErrorMessage();
            }

            @Override
            public void prepareFailView(EditTagOutputData failedOutput) {
                errorMsg[0] = failedOutput.getErrorMessage();
            }
        };

        EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter, new DeleteAndCreate());
        EditTagController controller = new EditTagController(tagDao, manageTagsViewModel, interactor);

        CustomTag oldTag = new CustomTag("home", HOUSE);
        CustomTag newTag = new CustomTag("home", MUSCLE);

        controller.execute(oldTag, newTag);

        Map<String, String> tags = tagDao.getCustomTags(username);
        boolean containsName = tags.containsKey("home");
        boolean containsNewIcon = tags.get("home").equals(MUSCLE);

        assert containsName && containsNewIcon;
    }

    @Test
    public void testSameIconDiffName () {
        TagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        String[] errorMsg = new String[1];

        EditTagOutputBoundary presenter = new EditTagOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTagOutputData successOutput) {
                errorMsg[0] = successOutput.getErrorMessage();
            }

            @Override
            public void prepareFailView(EditTagOutputData failedOutput) {
                errorMsg[0] = failedOutput.getErrorMessage();
            }
        };

        EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter, new DeleteAndCreate());
        EditTagController controller = new EditTagController(tagDao, manageTagsViewModel, interactor);

        CustomTag oldTag = new CustomTag("home", HOUSE);
        tagDao.addCustomTag(username, oldTag);
        CustomTag newTag = new CustomTag("house", HOUSE);

        controller.execute(oldTag, newTag);

        Map<String, String> tags = tagDao.getCustomTags(username);
        boolean notContainsOldName = !tags.containsKey("home");
        boolean containsNewName = tags.containsKey("house");
        boolean containsSameIcon = tags.get("house").equals(HOUSE);

        assert notContainsOldName && containsNewName && containsSameIcon;
    }

    @Test
    public void testEditTakenName () {
        TagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        final String[] errorMsg = new String[1];

        EditTagOutputBoundary presenter = new EditTagOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTagOutputData successOutput) {
                errorMsg[0] = successOutput.getErrorMessage();
            }

            @Override
            public void prepareFailView(EditTagOutputData failedOutput) {
                errorMsg[0] = failedOutput.getErrorMessage();
            }
        };

        EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter, new DeleteAndCreate());
        EditTagController controller = new EditTagController(tagDao, manageTagsViewModel, interactor);

        CustomTag TagA = new CustomTag("home", HOUSE);
        tagDao.addCustomTag(username, TagA);

        CustomTag TagB = new CustomTag("gym", MUSCLE);
        tagDao.addCustomTag(username, TagB);

        CustomTag TagC = new CustomTag("home", FOOD);

        controller.execute(TagB, TagC);

        assert errorMsg[0] == "Tag name is taken";

    }

    @Test
    public void testEditTakenIcon () {
        TagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        final String[] errorMsg = new String[1];

        EditTagOutputBoundary presenter = new EditTagOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTagOutputData successOutput) {
                errorMsg[0] = successOutput.getErrorMessage();
            }

            @Override
            public void prepareFailView(EditTagOutputData failedOutput) {
                errorMsg[0] = failedOutput.getErrorMessage();
            }
        };

        EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter, new DeleteAndCreate());
        EditTagController controller = new EditTagController(tagDao, manageTagsViewModel, interactor);

        CustomTag TagA = new CustomTag("home", HOUSE);
        tagDao.addCustomTag(username, TagA);

        CustomTag TagB = new CustomTag("gym", MUSCLE);
        tagDao.addCustomTag(username, TagB);

        CustomTag TagC = new CustomTag("house", HOUSE);

        controller.execute(TagB, TagC);

        assert errorMsg[0] == "Tag icon is taken";
    }


}
