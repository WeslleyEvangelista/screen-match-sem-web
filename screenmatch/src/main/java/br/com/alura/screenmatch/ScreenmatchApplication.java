package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.DataConversion;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var apiConsumption = new ApiConsumption();
		var apiKey = "&apikey=ddf8730b";
		var titleName = "?t=Game+of+thrones";
		var url = "https://www.omdbapi.com/" + titleName + apiKey;
		var json = apiConsumption.getData(url);

		System.out.println(json);

		DataConversion conversion = new DataConversion();
		SeriesData data = conversion.getData(json, SeriesData.class);

		System.out.println(data);
	}
}
