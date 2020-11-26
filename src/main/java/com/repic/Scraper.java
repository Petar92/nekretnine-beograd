package com.repic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Scraper {
	
	public void scrapeNekretnineRS() {
		
		Integer pageNumber = 1;
		String baseUrl = "https://www.nekretnine.rs/" ;
		List<String> results = new ArrayList<>();
		
		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		while (true) {
			try {  
			  String searchUrl = "https://www.nekretnine.rs/stambeni-objekti/stanovi/izdavanje-prodaja/prodaja/grad/beograd/lista/po-stranici/10/stranica/"
					  + pageNumber + "/";
			  HtmlPage page = client.getPage(searchUrl);
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
						
						itemArea = itemArea.replace(" ", "");
						itemArea = itemArea.replace("---", "0");
						itemArea = itemArea.replace("m2", "");
						item.setArea(new BigDecimal(itemArea));
						
						itemPrice = itemPrice.replace("---", "0");
						itemPrice = itemPrice.replace("EUR", "");
						itemPrice = itemPrice.replace(" ", "");				
						item.setPrice(new BigDecimal(itemPrice));
					
						
						ObjectMapper mapper = new ObjectMapper();
						String jsonString = mapper.writeValueAsString(item);
						results.add(jsonString);
						//System.out.println(jsonString);
					}
					pageNumber++;
				}
			}catch(Exception e){
			  e.printStackTrace();
			}
		}
		System.out.println(results);
		System.out.println("result size: " + results.size());
	}
	public static void main(String[] args) {
		Scraper s = new Scraper();
		s.scrapeNekretnineRS();
	}
}
