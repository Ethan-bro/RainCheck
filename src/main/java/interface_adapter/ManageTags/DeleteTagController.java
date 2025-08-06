package interface_adapter.ManageTags;

import use_case.createCustomTag.CustomTagDataAccessInterface;
import entity.CustomTag;

public class DeleteTagController {

    private final CustomTagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsVM;

    public DeleteTagController(CustomTagDataAccessInterface tagDao, ManageTagsViewModel manageTagsVM) {
        this.tagDao = tagDao;
        this.manageTagsVM = manageTagsVM;
    }

    /**
     * Deletes a custom tag by name.
     *
     * @param tagName name of tag to delete
     */
    public void execute(String tagName) {
        if (tagName == null || tagName.isEmpty()) return;

        // Find emoji for tagName in current tags
        String emoji = manageTagsVM.getTagEmojiFor(tagName);
        if (emoji == null) return;

        tagDao.deleteCustomTag(manageTagsVM.getUsername(), new CustomTag(tagName, emoji));

        // Refresh view model data
        manageTagsVM.refreshTags();
    }
}
