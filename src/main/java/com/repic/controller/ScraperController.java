package com.repic.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.repic.model.Item;
import com.repic.model.Scraper;
import com.repic.service.ItemService;

@RestController
public class ScraperController {
	
	private final Scraper scraper;
	
	private final ItemService itemService;

	public ScraperController(Scraper scraper, ItemService itemService) {
		this.scraper = scraper;
		this.itemService = itemService;
	}
	
	@GetMapping("/all")
	public List<String> nekretnine(){
		return scraper.scrapeNekretnineRS(10);
	}
	
	@GetMapping("/nekretnine")
	public List<Item> getAll() {
		return itemService.getAll();
	}
	
	@GetMapping("/nekretnine/{id}")
	public Optional<Item> getItem(@PathVariable Long id) {
		return itemService.getItem(id);
	}
	
	@PostMapping("/nekretnine")
	public void insert(@RequestBody Item item) {
		itemService.addItem(item);
	}
	
	@PutMapping("/nekretnine")
	public void update(@RequestBody Item item) {
		itemService.updateItem(item);
	}
	
	@DeleteMapping("/nekretnine/{id}")
	public void delete(@PathVariable Long id) {
		itemService.deleteItem(id);
	}
	
	

}
