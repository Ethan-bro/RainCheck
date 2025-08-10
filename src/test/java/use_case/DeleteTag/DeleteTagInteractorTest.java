package use_case.DeleteTag;

import data_access.InMemoryTagDataAccessObject;
import entity.CustomTag;
import interface_adapter.DeleteTag.DeleteTagController;
import interface_adapter.ManageTags.ManageTagsViewModel;
import org.junit.jupiter.api.Test;
import use_case.CreateTag.TagDataAccessInterface;

import java.util.Map;

import static use_case.CreateTag.TagIcons.HOUSE;

public class DeleteTagInteractorTest {

    private final String username = "Boris";

    @Test
    public void testSuccessfulDelete() {

        TagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        String[] errorMsg = new String[1];

        // create the interactor
        DeleteTagOutputBoundary presenter = new DeleteTagOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteTagOutputData successOutput) {
                errorMsg[0] = successOutput.getMessage();
            }
        };

            DeleteTagInputBoundary interactor = new DeleteTagInteractor(presenter, tagDao);
            DeleteTagController controller = new DeleteTagController(tagDao, manageTagsViewModel, interactor);

            // create tag to delete
            CustomTag tag = new CustomTag("home", HOUSE);

        controller.execute(tag);

            // fetch user's tags
            Map<String, String> tags = tagDao.getCustomTags(username);

            // check if tag was deleted
            boolean notContainsName = !tags.containsKey("home");
            boolean notContainsIcon = !tags.containsValue(HOUSE);

        assert(notContainsName &&notContainsIcon &&errorMsg[0]=="success");

        }
    }
