package com.repic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.repic.model.Item;

@Component
public class ItemService {

	private ItemRepository itemRepository;
	
	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}
	
	public List<Item> getAll() {
		List<Item> items = new ArrayList<>();
		itemRepository.findAll().forEach(items::add);
		return items;
	}
	
	public Optional<Item> getItem(Long id) {
		return itemRepository.findById(id);
	}
	
	public void addItem(Item item) {
		itemRepository.save(item);
	}
	
	public void updateItem(Item item) {
		itemRepository.save(item);
	}
	
	public void deleteItem(Long id) {
		itemRepository.deleteById(id);
	}

}
