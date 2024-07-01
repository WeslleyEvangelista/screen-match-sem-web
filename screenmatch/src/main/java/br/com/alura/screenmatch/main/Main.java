package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.DataConversion;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ApiConsumption consumption = new ApiConsumption();
    private DataConversion conversion = new DataConversion();
    private final String URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ddf8730b";
    private List<SeriesData> seriesData = new ArrayList<>();

    public void displayMenu() {
        var option = -1;

        while(option != 0) {
            var menu = """
                    1 - Search for series
                    2 - Search for series episodes
                    3 - List searched series
                                   \s
                    0 - Exit
                   \s""";

            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    searchSeriesWeb();
                    break;
                case 2:
                    searchEpisodeBySeries();
                    break;
                case 3:
                    listSearchedSeries();
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void searchSeriesWeb() {
        SeriesData data = getSeriesData();
        seriesData.add(data);
        System.out.println(data);
    }

    private SeriesData getSeriesData() {
        System.out.println("Insert the series name to search: ");
        var seriesName = scanner.nextLine();
        var json = consumption.getData(URL + seriesName.replace(" ", "+") + API_KEY);
        SeriesData data = conversion.getData(json, SeriesData.class);
        return data;
    }

    private void searchEpisodeBySeries() {
        SeriesData seriesData = getSeriesData();
        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= seriesData.totalSeasons(); i++) {
            var json = consumption.getData(URL + seriesData.title().replace(" ", "+") + API_KEY);
            SeasonData seasonData = conversion.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }

    private void listSearchedSeries() {
        List<Series> series = new ArrayList<>();
        series = seriesData.stream()
                        .map(d -> new Series(d))
                                .collect(Collectors.toList());
        series.stream()
                .sorted(Comparator.comparing(Series::getGender))
                .forEach(System.out::println);
    }
}
