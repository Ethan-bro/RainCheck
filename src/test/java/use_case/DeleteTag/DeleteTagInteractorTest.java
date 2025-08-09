package use_case.DeleteTag;

import data_access.InMemoryTagDataAccessObject;
import entity.CustomTag;
import interface_adapter.deleteCustomTag.DeleteTagController;
import interface_adapter.manageTags.ManageTagsViewModel;
import org.junit.jupiter.api.Test;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.createCustomTag.CustomTagIcons;
import use_case.deleteCustomTag.DeleteCustomTagInputBoundary;
import use_case.deleteCustomTag.DeleteCustomTagInteractor;
import use_case.deleteCustomTag.DeleteCustomTagOutputBoundary;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteTagInteractorTest {

    private final String username = "Boris";

    @Test
    void testSuccessfulDelete() {

        CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        final String[] errorMsg = new String[1];

        DeleteCustomTagOutputBoundary presenter = successOutput -> errorMsg[0] = successOutput.getMessage();

        DeleteCustomTagInputBoundary interactor = new DeleteCustomTagInteractor(presenter, tagDao);
        DeleteTagController controller = new DeleteTagController(tagDao, manageTagsViewModel, interactor);

        // create tag to delete
        CustomTag tag = new CustomTag("home", CustomTagIcons.HOUSE);

        controller.execute(tag);

        // fetch user's tags
        Map<String, String> tags = tagDao.getCustomTags(username);

        // check if tag was deleted
        boolean notContainsName = !tags.containsKey("home");
        boolean notContainsIcon = !tags.containsValue(CustomTagIcons.HOUSE);

        assertTrue(notContainsName, "Tag name 'home' should be deleted");
        assertTrue(notContainsIcon, "Tag icon HOUSE should be deleted");
        assertEquals("success", errorMsg[0], "Output message should be 'success'");
    }
}
