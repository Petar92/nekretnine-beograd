package com.repic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.repic.NekretnineBeogradApplication;
import com.repic.service.ItemRepository;
import com.repic.service.ItemService;

@Configuration
@ComponentScan(basePackageClasses = NekretnineBeogradApplication.class)
@PropertySource("classpath:/application.properties")
@EnableWebMvc
public class ApplicationConfiguration {
	
	public ItemService itemService(ItemRepository itemRepository) {
		return new ItemService(itemRepository);
	}

}
