package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.model.SeriesEpisodeData;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.DataConversion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
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

        System.out.println("Top 5 episodes: ");
        episodesData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(SeriesEpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
}
