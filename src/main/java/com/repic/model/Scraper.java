package com.repic.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.repic.service.ItemService;

@Component
public class Scraper {
	
	private List<String> results = Collections.synchronizedList(new ArrayList<>());
	private ItemService itemService;
	
	public Scraper(ItemService itemService) {
		this.itemService = itemService;
	}
	
	public List<String> scrapeNekretnineRS(int numberOfThreads) {
		
		Integer totalItems;
		Integer totalPages;
		
		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
				
		try {
			  String searchUrl = "https://www.nekretnine.rs/stambeni-objekti/stanovi/izdavanje-prodaja/prodaja/grad/beograd/lista/po-stranici/10/stranica/";
			  HtmlPage page = client.getPage(searchUrl);
			  
			  HtmlElement totalElements = ((HtmlElement) page.getFirstByXPath(".//a[@class='radio filter-radio active']/span"));
			  
			  String te = totalElements.asText();
			  te = te.replace("(", "");
			  te = te.replace(")", "");
			  totalItems = Integer.parseInt(te);
			  System.out.println("total items " + totalItems);
			  totalPages = 500; //(int) Math.ceil(totalItems / 20.0);
			  
			  List<Thread> threads = new ArrayList<>();
				for(int i = 0; i < numberOfThreads ; i++) {
					int div = (int) (Math.ceil((double) totalPages / numberOfThreads));
					int startPage = i * div + 1;
					int endPage = i * div + div;
					Thread thread = new Thread(() -> {
						scrapePages(startPage, endPage, searchUrl, client, results);
					});
					threads.add(thread);
				}
				
		        for(Thread thread : threads) {
		            thread.start();
		        }

				
				for(Thread thread : threads) { 
					try { 
						thread.join(); 
					} catch(InterruptedException e) { } 
				}
			  
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return results;
	}
		
	private void scrapePages(int startPage, int endPage, String searchUrl, WebClient client, List<String> results) {
		try {
			
		  int currentPage = startPage;	
		  searchUrl = "https://www.nekretnine.rs/stambeni-objekti/stanovi/izdavanje-prodaja/prodaja/grad/beograd/lista/po-stranici/10/stranica/" + currentPage + "/";
		  
		  HtmlPage page = client.getPage(searchUrl);
		  
		  while (currentPage <= endPage) {
			  List<HtmlElement> items = (List<HtmlElement>) (Object) page.getByXPath("//div[@class='row offer']") ;
			//  if(items.isEmpty()){
			//		System.out.println("breaking...");
			//		break;
			//  }else{
					for(HtmlElement htmlItem : items){
						HtmlElement spanArea = ((HtmlElement) htmlItem.getFirstByXPath(".//p[@class='offer-price offer-price--invert']/span"));
						HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//p[@class='offer-price']/span")) ;
						HtmlElement spanDate = ((HtmlElement) htmlItem.getFirstByXPath(".//div[@class='mt-1 mb-1 mt-lg-0 mb-lg-0 d-md-block offer-meta-info offer-adress']")) ;
						HtmlElement spanLocation = ((HtmlElement) htmlItem.getFirstByXPath(".//p[@class='offer-location text-truncate']")) ;
						
						// It is possible that an item doesn't have area, we set the area to 0.0 in this case
						String itemArea = spanArea == null ? "0.0" : spanArea.asText() ;
						
						// It is possible that an item doesn't have any price, we set the price to 0.0 in this case
						String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText() ;
						
						String itemDate = spanDate == null ? "" : spanDate.asText();
						
						String itemLocation = spanLocation == null ? "" : spanLocation.asText();
					
						Item item = new Item();
						itemArea = itemArea.replace(" ", "");
						itemArea = itemArea.replace("---", "0");
						itemArea = itemArea.replace("m2", "");
						if (itemArea.equals("0")) continue;
						item.setArea(new BigDecimal(itemArea));
						
						itemPrice = itemPrice.replace("---", "0");
						itemPrice = itemPrice.replace("EUR", "");
						itemPrice = itemPrice.replace(" ", "");
						itemPrice = itemPrice.replace("Po dogovoru", "");
						if (itemPrice.equals("0")) continue;
						item.setPrice(new BigDecimal(itemPrice));
						
						itemDate = itemDate.substring(0, 10).replace(".", "/");
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						LocalDate dateTime = LocalDate.parse(itemDate, formatter);
					
						item.setDate(dateTime);
						
						itemLocation = itemLocation.replace(", Beograd, Srbija", "");						
						item.setLocation(itemLocation);
						
						ObjectMapper mapper = new ObjectMapper();
						String jsonString = mapper.writeValueAsString(item);
						synchronized(results) {
							itemService.addItem(item);
							System.out.println("added to database...");
							results.add(jsonString);
						}
					}
					synchronized(results) {
						currentPage++;}
			  //}
		  }
		}catch(Exception e){
		  e.printStackTrace();
		  System.exit(0);
		}
	}
	/*
	 * public static void main(String[] args) { PrintStream originalOut =
	 * System.out; PrintStream fileOut; try { fileOut = new
	 * PrintStream("/home/petar/out.txt"); System.setOut(fileOut); Scraper s = new
	 * Scraper(); s.scrapeNekretnineRS(10); } catch (FileNotFoundException e) {
	 * e.printStackTrace(); System.exit(0); }
	 * 
	 * }
	 */
}
