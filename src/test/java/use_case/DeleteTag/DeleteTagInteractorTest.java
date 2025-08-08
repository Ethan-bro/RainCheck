package use_case.DeleteTag;

import data_access.InMemoryTagDataAccessObject;
import entity.CustomTag;
import interface_adapter.DeleteCT.DeleteTagController;
import interface_adapter.EditTag.EditTagController;
import interface_adapter.ManageTags.ManageTagsViewModel;
import org.junit.jupiter.api.Test;
import use_case.CreateCT.CustomTagDataAccessInterface;
import use_case.DeleteCT.DeleteCTInputBoundary;
import use_case.DeleteCT.DeleteCTInteractor;
import use_case.DeleteCT.DeleteCTOutputBoundary;
import use_case.DeleteCT.DeleteCTOutputData;
import use_case.EditCT.EditTagInputBoundary;
import use_case.EditCT.EditTagInteractor;
import use_case.EditCT.EditTagOutputBoundary;
import use_case.EditCT.EditTagOutputData;

import java.util.Map;

import static use_case.CreateCT.CustomTagIcons.HOUSE;
import static use_case.CreateCT.CustomTagIcons.MUSCLE;

public class DeleteTagInteractorTest {

    private final String username = "Boris";

    @Test
    public void testSuccessfulDelete() {

        CustomTagDataAccessInterface tagDao = new InMemoryTagDataAccessObject();
        ManageTagsViewModel manageTagsViewModel = new ManageTagsViewModel(tagDao, username);
        String[] errorMsg = new String[1];

        // create the interactor
        DeleteCTOutputBoundary presenter = new DeleteCTOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteCTOutputData successOutput) {
                errorMsg[0] = successOutput.getMessage();
            }
        };

            DeleteCTInputBoundary interactor = new DeleteCTInteractor(presenter, tagDao);
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
