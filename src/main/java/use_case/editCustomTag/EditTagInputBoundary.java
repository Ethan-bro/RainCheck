package use_case.editCustomTag;

/**
 * Input boundary interface for the EditTag use case.
 * Defines the method to execute the editing of a custom tag.
 */
public interface EditTagInputBoundary {

    /**
     * Executes the edit tag use case with the given input data.
     *
     * @param input the input data containing details for editing the custom tag
     */
    void execute(EditTagInputData input);
}
