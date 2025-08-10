package use_case.editCustomTag.TagReplacement;

import entity.CustomTag;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.editCustomTag.EditTagInputData;

public interface TagReplacementStrategy {

    public Boolean replaceTag(EditTagInputData inputData, CustomTagDataAccessInterface tagDao);

    public String getStatusMsg();

    public CustomTag getCreatedTag();
}
