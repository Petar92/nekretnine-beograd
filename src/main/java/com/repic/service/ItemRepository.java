package com.repic.service;

import org.springframework.data.repository.CrudRepository;

import com.repic.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {
	
}
