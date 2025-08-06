package interface_adapter.ManageTags;

import entity.CustomTag;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.createCustomTag.CustomTagDataAccessInterface;

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
        if (oldName.equals(newName)) return; // No change

        String username = manageTagsVM.getUsername();

        // Delete old tag
        tagDao.deleteCustomTag(username, new CustomTag(oldName, emoji));

        // Add new tag with the new name and same emoji
        tagDao.addCustomTag(username, new CustomTag(newName, emoji));

        // Notify all subscribed views/viewModels of tag changes
        TagChangeEventNotifier.fire();
    }
}
