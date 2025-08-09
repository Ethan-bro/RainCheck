package interface_adapter.manageTags;

import entity.CustomTag;

import interface_adapter.events.TagChangeEventNotifier;

import use_case.createCustomTag.CustomTagDataAccessInterface;

/**
 * Controller responsible for deleting custom tags.
 */
public class DeleteTagController {

    private final CustomTagDataAccessInterface tagDao;
    private final ManageTagsViewModel manageTagsViewModel;

    public DeleteTagController(CustomTagDataAccessInterface tagDao, ManageTagsViewModel manageTagsViewModel) {
        this.tagDao = tagDao;
        this.manageTagsViewModel = manageTagsViewModel;
    }

    /**
     * Deletes a custom tag by name.
     *
     * @param tagName name of tag to delete
     */
    public void execute(String tagName) {
        if (tagName != null && !tagName.isEmpty()) {
            final String emoji = manageTagsViewModel.getTagEmojiFor(tagName);
            if (emoji != null) {
                tagDao.deleteCustomTag(manageTagsViewModel.getUsername(), new CustomTag(tagName, emoji));

                // Notify global listeners that tags have changed
                TagChangeEventNotifier.fire();
            }
        }
    }
}
