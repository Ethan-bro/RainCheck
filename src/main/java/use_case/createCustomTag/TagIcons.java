package use_case.createCustomTag;

import java.util.List;

public class TagIcons {
    private TagIcons() {}

    public static final String BOOKS = "📚";
    public static final String PLANE = "✈️";
    public static final String HOUSE = "🏡";
    public static final String HEART = "❤️";
    public static final String RING = "💍";
    public static final String MUSCLE = "💪";
    public static final String MUSIC = "🎶";
    public static final String FOOD = "🍽️";
    public static final String NATURE = "🏔️";

    public static final List<String> IconList = List.of(
            BOOKS,
            PLANE,
            HOUSE,
            HEART,
            RING,
            MUSCLE,
            MUSIC,
            FOOD,
            NATURE
    );
}
