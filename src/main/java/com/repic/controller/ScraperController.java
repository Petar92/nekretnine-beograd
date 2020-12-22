package com.repic.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repic.service.Scraper;

@RestController
public class ScraperController {
	
	private final Scraper scraper;

	public ScraperController(Scraper scraper) {
		this.scraper = scraper;
	}
	
	@GetMapping("/all")
	public List<String> nekretnine(){
		return scraper.scrapeNekretnineRS(10);
	}

}
