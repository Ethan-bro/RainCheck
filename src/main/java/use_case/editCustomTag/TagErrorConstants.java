package use_case.editCustomTag;

/**
 * A container for tag-related error message constants.
 * This class is final and has a private constructor because it is intended
 * to be used only as a static constants holder. The constants represent
 * standard error messages related to custom tag editing and validation.
 */
public final class TagErrorConstants {

    /** Error message when a tag name is already taken. */
    public static final String NAME_TAKEN_ERROR = "Tag name is taken";

    /** Error message when a tag icon is already taken. */
    public static final String ICON_TAKEN_ERROR = "Tag icon is taken";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private TagErrorConstants() {
    }

    /**
     * Gets the standard error message for when a tag name is already in use.
     *
     * @return the error message indicating the tag name is taken
     */
    public static String getNameTakenError() {
        return NAME_TAKEN_ERROR;
    }

    /**
     * Gets the standard error message for when a tag icon is already in use.
     *
     * @return the error message indicating the tag icon is taken
     */
    public static String getIconTakenError() {
        return ICON_TAKEN_ERROR;
    }
}
