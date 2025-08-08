package use_case.createCustomTag;

import java.util.List;

/**
 * Utility class containing constants for custom tag icons.
 */
public final class CustomTagIcons {

    public static final String BOOKS = "📚";
    public static final String FOOD = "🍽️";
    public static final String HEART = "❤️";
    public static final String HOUSE = "🏡";
    public static final String MUSIC = "🎶";
    public static final String MUSCLE = "💪";
    public static final String NATURE = "🏔️";
    public static final String PLANE = "✈️";
    public static final String RING = "💍";
    public static final String STAR = "⭐️";

    public static final List<String> ICON_LIST = List.of(
            BOOKS,
            FOOD,
            HEART,
            HOUSE,
            MUSIC,
            MUSCLE,
            NATURE,
            PLANE,
            RING,
            STAR
    );

    private CustomTagIcons() {
        // Prevent instantiation
    }

    public static List<String> getIconList() {
        return ICON_LIST;
    }
}
