package com.repic;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Scraper {
	
	private List<String> results = Collections.synchronizedList(new ArrayList<>());
	
	public void scrapeNekretnineRS(int numberOfThreads) {
		
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
						long start = System.nanoTime();
						scrapePage(startPage, endPage, searchUrl, client, results);
						long end = System.nanoTime();
						System.out.println("thread time " + (end - start) / 1000000000);
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
		}
	}
		
	private static void scrapePage(int startPage, int endPage, String searchUrl, WebClient client, List<String> results) {
		try {
		  int currentPage = startPage;	
		  searchUrl = "https://www.nekretnine.rs/stambeni-objekti/stanovi/izdavanje-prodaja/prodaja/grad/beograd/lista/po-stranici/10/stranica/" + currentPage + "/";
		  //System.out.println("SEARCH URL ---------------------->" + searchUrl);
		  HtmlPage page = client.getPage(searchUrl);
		  
		  while (currentPage <= endPage) {
			  System.out.println("current page: " + currentPage + ", end page: " + endPage);
			  List<HtmlElement> items = (List<HtmlElement>) (Object) page.getByXPath("//div[@class='row offer']") ;
			  if(items.isEmpty()){
					System.out.println("breaking...");
					break;
			  }else{
					for(HtmlElement htmlItem : items){
						HtmlElement spanArea = ((HtmlElement) htmlItem.getFirstByXPath(".//p[@class='offer-price offer-price--invert']/span"));
						HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//p[@class='offer-price']/span")) ;
						
						// It is possible that an item doesn't have area, we set the area to 0.0 in this case
						String itemArea = spanArea == null ? "0.0" : spanArea.asText() ;
						
						// It is possible that an item doesn't have any price, we set the price to 0.0 in this case
						String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText() ;
						
						Item item = new Item();
						synchronized(results) {
						itemArea = itemArea.replace(" ", "");
						itemArea = itemArea.replace("---", "0");
						itemArea = itemArea.replace("m2", "");
						item.setArea(new BigDecimal(itemArea));
						
						itemPrice = itemPrice.replace("---", "0");
						itemPrice = itemPrice.replace("EUR", "");
						itemPrice = itemPrice.replace(" ", "");
						itemPrice = itemPrice.replace("Po dogovoru", "");	
						item.setPrice(new BigDecimal(itemPrice));
					
						
						ObjectMapper mapper = new ObjectMapper();
						String jsonString = mapper.writeValueAsString(item);
						
							results.add(jsonString);
						}
						//System.out.println(jsonString);
					}
					currentPage++;
			  }
		  }
		}catch(Exception e){
		  e.printStackTrace();
		}
		//System.out.println(results);
synchronized(results) {
		System.out.println("result size: " + results.size());
}
	}
	public static void main(String[] args) {
		PrintStream originalOut = System.out;
		PrintStream fileOut;
		try {
			fileOut = new PrintStream("/home/petar/out.txt");
			System.setOut(fileOut);
			Scraper s = new Scraper();
			s.scrapeNekretnineRS(10);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
