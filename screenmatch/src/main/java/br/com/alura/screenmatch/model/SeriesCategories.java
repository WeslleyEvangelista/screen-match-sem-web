package br.com.alura.screenmatch.model;

public enum SeriesCategories {
    ACTION("Action"),
    ROMANCE("Romance"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime");

    private String omdbCategory;

    SeriesCategories(String omdbCategory) {
        this.omdbCategory = omdbCategory;
    }

    public static SeriesCategories fromString(String text) {
        for (SeriesCategories category: SeriesCategories.values()) {
            if (category.omdbCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No categories were found for this series");
    }
}
