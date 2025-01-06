package at.backend.tourist.places;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "at.backend.tourist.places")
public class TouristPlacesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TouristPlacesApplication.class, args);
	}

}
