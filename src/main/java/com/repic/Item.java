package com.repic;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Item {
	
	private BigDecimal area;
	private BigDecimal price ;
	private LocalDate date;
	private String location;
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getArea() {
		return area;
	}
	
	public void setArea(BigDecimal area) {
		this.area = area;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
