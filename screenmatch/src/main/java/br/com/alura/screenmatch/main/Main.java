package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.model.SeriesEpisodeData;
import br.com.alura.screenmatch.model.SeriesEpisodes;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.DataConversion;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ApiConsumption consumption = new ApiConsumption();
    private DataConversion conversion = new DataConversion();
    private final String URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ddf8730b";

    public void displayMenu() {
        System.out.println("Type the movie/series you wish to search: ");
        var titleName = scanner.nextLine();

        var json = consumption.getData(URL + titleName.replace(" ", "+") + API_KEY);
        SeriesData data = conversion.getData(json, SeriesData.class);

        System.out.println(data);

        List<SeasonData> seasons = new ArrayList<>();

		for (int i = 1; i < data.totalSeasons() ; i++) {
			json = consumption.getData(URL + titleName.replace(" ", "+") + "&season=" + i + API_KEY);
			SeasonData seasonData = conversion.getData(json, SeasonData.class);

			seasons.add(seasonData);
		}

		seasons.forEach(System.out::println);

        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));

        List<SeriesEpisodeData> episodesData = seasons.stream()
                .flatMap(s -> s.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("Top 10 episodes: ");
        episodesData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("First filter(N/A) " + e))
                .sorted(Comparator.comparing(SeriesEpisodeData::rating).reversed())
                .peek(e -> System.out.println("Ordered " + e))
                .limit(10)
                .peek(e -> System.out.println("Limit " + e))
                .map(e -> e.title().toUpperCase())
                .peek(e -> System.out.println("Map " + e))
                .forEach(System.out::println);

        List<SeriesEpisodes> episodes = seasons.stream()
                .flatMap(s -> s.episodes().stream().map(d -> new SeriesEpisodes(s.number(), d)))
                .collect(Collectors.toList());

        episodes.forEach(System.out::println);

        System.out.println("Enter an title excerpt of the episode of the series that you are looking for: ");
        var titlePart = scanner.nextLine();

        Optional<SeriesEpisodes> searchedEpisode = episodes.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(titlePart.toUpperCase()))
                .findFirst();

        if (searchedEpisode.isPresent()) {
            System.out.println("Episode found!");
            System.out.println("Season: " + searchedEpisode.get().getSeasonNumber());
        } else {
            System.out.println("Episode not found!");
        }

        System.out.println("Search episodes by year: ");
        var year = scanner.nextInt();
        scanner.nextLine();

        LocalDate searchDate = LocalDate.of(year,1,1);

        episodes.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
                .forEach(e -> System.out.println(
                        "Season: " + e.getSeasonNumber() +
                                " | Episode: " + e.getTitle() +
                                " | Release Date: " + e.getReleaseDate()
                ));

        Map<Integer, Double> ratingBySeason = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(
                                SeriesEpisodes::getSeasonNumber,
                                Collectors.averagingDouble(SeriesEpisodes::getRating)));

        System.out.println(ratingBySeason);

        DoubleSummaryStatistics statistics = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(SeriesEpisodes::getRating));

        System.out.println("Average rating: " + statistics.getAverage());
        System.out.println("Lowest rating: " + statistics.getMin());
        System.out.println("Highest rating: " + statistics.getMax());
        System.out.println("Total episodes rated: " + statistics.getCount());
    }
}
