public enum Rubric {
    SCIENCE(0, "science"),
    SPORT(1, "sport"),
    CULTURE(2, "culture"),
    MEDIA(3, "media"),
    ECONOMICS(4, "economics"),
    POLITICS(5, "politics"),
    SOCIETY(6, "society");

    static final String[] rubrics = new String[] {
            "science",
            "sport",
            "culture",
            "media",
            "economics",
            "politics",
            "society",
    };

    public final int value;
    public final String name;

    Rubric(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getNameByValue(int value) {
        return rubrics[value];
    }
}