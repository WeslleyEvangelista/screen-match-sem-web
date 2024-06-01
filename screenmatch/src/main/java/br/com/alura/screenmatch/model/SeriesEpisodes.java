package br.com.alura.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SeriesEpisodes {
    private Integer seasonNumber;
    private String title;
    private Integer episodeNumber;
    private Double rating;
    private LocalDate releaseDate;

    public SeriesEpisodes(Integer seasonNumber, SeriesEpisodeData episodeData) {
        this.seasonNumber = seasonNumber;
        this.title = episodeData.title();
        this.episodeNumber = episodeData.number();
        try{
            this.rating = Double.valueOf(episodeData.rating());
        } catch (NumberFormatException e){
            this.rating = 0.0;
        }
        try {
            this.releaseDate = LocalDate.parse(episodeData.releaseDate());
        } catch (DateTimeParseException e){
            this.releaseDate = null;
        }
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "SeriesEpisodes{" +
                "seasonNumber=" + seasonNumber +
                ", title='" + title + '\'' +
                ", episodeNumber=" + episodeNumber +
                ", rating=" + rating +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
