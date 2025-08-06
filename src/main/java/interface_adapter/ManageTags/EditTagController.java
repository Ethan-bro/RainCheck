package interface_adapter.ManageTags;

import use_case.createCustomTag.CustomTagDataAccessInterface;
import entity.CustomTag;

public class EditTagController {

    private final CustomTagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsVM;

    public EditTagController(CustomTagDataAccessInterface tagDao, ManageTagsViewModel manageTagsVM) {
        this.tagDao = tagDao;
        this.manageTagsVM = manageTagsVM;
    }

    /**
     * Rename an existing tag while keeping the emoji.
     * Effectively deletes old tag and adds a new one.
     *
     * @param oldName current tag name
     * @param newName new tag name
     * @param emoji   emoji associated with the tag
     */
    public void execute(String oldName, String newName, String emoji) {
        if (oldName == null || newName == null || emoji == null) return;
        if (oldName.equals(newName)) return; // no change

        // Delete old tag
        tagDao.deleteCustomTag(manageTagsVM.getUsername(), new CustomTag(oldName, emoji));

        // Add new tag with new name and same emoji
        tagDao.addCustomTag(manageTagsVM.getUsername(), new CustomTag(newName, emoji));

        // Refresh view model data
        manageTagsVM.refreshTags();
    }
}
