package com.repic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.repic.service.Scraper;

@SpringBootApplication
public class NekretnineBeogradApplication {

	public static void main(String[] args) {
		SpringApplication.run(NekretnineBeogradApplication.class, args);
		Scraper scraper = new Scraper();
		scraper.scrapeNekretnineRS(10);
		
	}

}
