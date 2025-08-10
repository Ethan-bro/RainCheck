package use_case.edit_custom_tag.tagReplacement;

import entity.CustomTag;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.edit_custom_tag.EditTagInputData;

public interface TagReplacementStrategy {

    public Boolean replaceTag(EditTagInputData inputData, CustomTagDataAccessInterface tagDao);

    public String getStatusMsg();

    public CustomTag getCreatedTag();
}
