package use_case.EditTag.TagReplacement;

import entity.CustomTag;
import use_case.CreateTag.TagDataAccessInterface;
import use_case.EditTag.EditTagInputData;
import use_case.EditTag.EditTagOutputData;

import java.util.Map;

import static use_case.EditTag.TagErrorConstants.ICON_TAKEN_ERROR;
import static use_case.EditTag.TagErrorConstants.NAME_TAKEN_ERROR;

public interface TagReplacementStrategy {

    public Boolean replaceTag(EditTagInputData inputData, TagDataAccessInterface tagDao);

    public String getStatusMsg();

    public CustomTag getCreatedTag();
}
