package com.repic;

import java.math.BigDecimal;

public class Item {
	
	private BigDecimal area;
	private BigDecimal price ;
	
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

}
