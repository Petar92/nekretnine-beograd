package com.repic.service;

import org.springframework.data.repository.CrudRepository;

import com.repic.model.Item;

public interface ItemRepository extends CrudRepository<Item, Long>{

}
