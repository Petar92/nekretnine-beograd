package com.repic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.repic.Item;

public class ItemService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	public List<Item> getAllItems() {
		List<Item> items = new ArrayList<>();
		itemRepository.findAll()
					  .forEach(items::add);
		return items;
	}
	
	public Optional<Item> getItem(long id) {
		return itemRepository.findById(id);
	}
	
	public void addItem(Item item) {
		itemRepository.save(item);
	}
	
	public void updateItem(Item item) {
		itemRepository.save(item);
	}
	
	public void deleteItem(long id) {
		itemRepository.deleteById(id);
	}

}
