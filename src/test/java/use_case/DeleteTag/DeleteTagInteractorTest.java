package use_case.DeleteTag;

import data_access.InMemoryTagDataAccessObject;
import entity.CustomTag;
import interface_adapter.DeleteCT.DeleteTagController;
import interface_adapter.ManageTags.ManageTagsViewModel;
import org.junit.jupiter.api.Test;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.DeleteCustomTag.DeleteCustomTagInputBoundary;
import use_case.DeleteCustomTag.DeleteCustomTagInteractor;
import use_case.DeleteCustomTag.DeleteCustomTagOutputBoundary;
import use_case.DeleteCustomTag.DeleteCustomTagOutputData;
import use_case.EditCT.EditTagInputBoundary;
import use_case.EditCT.EditTagInteractor;
import use_case.EditCT.EditTagOutputBoundary;
import use_case.EditCT.EditTagOutputData;

import java.util.Map;

import static use_case.createCustomTag.CustomTagIcons.HOUSE;

public class DeleteTagInteractorTest {

    private final String username = "Boris";

    @Test
    public void testSuccessfulDelete() {

        CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        String[] errorMsg = new String[1];

        // create the interactor
        DeleteCustomTagOutputBoundary presenter = new DeleteCustomTagOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteCustomTagOutputData successOutput) {
                errorMsg[0] = successOutput.getMessage();
            }
        };

            DeleteCustomTagInputBoundary interactor = new DeleteCustomTagInteractor(presenter, tagDao);
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
