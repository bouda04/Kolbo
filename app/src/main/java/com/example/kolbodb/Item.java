package com.example.kolbodb;




/**
 * This class saves details about a specific item that may be a product if a category
 *  it may have a part of its fields as default fields  , for instance fields bout sales price
 * in addition saves the version for case of updating the database
  */
public class Item {
	
	public static final String NONE_CATEGORY = "None";
	public static final String DEF_UNITS = "items";
	public String name; 
	public String category; 
	public double price; 
	public String image; 
	public int version;  
	public int discount_limit; 
	public double discount_price; 
	public boolean fruits_or_vegetibles; 
	public String units; 
	public Item()
	{
		category = NONE_CATEGORY; //unless specified later, default should be top level categories
		units = DEF_UNITS; 
	}
	public void setUnits(boolean fruits_or_vegetibles)
	{
		this.fruits_or_vegetibles = fruits_or_vegetibles; 
	}
	public void setDiscountPrice(double discount_price)
	{
		this.discount_price = discount_price; 
	}
	public void setDiscountLimit(int discount_limit)
	{
		this.discount_limit = discount_limit; 
	}
	public void setVersion(int version)
	{
		this.version = version; 
	}
	public void setName(String name)
	{
		this.name = name; 
	}
	public void setPrice(Double price)
	{
		this.price = price; 
	}
	public void setImage(String image)
	{
		this.image = image; 
	}
	public void setUnits(String units)
	{
		this.units = units; 
	}
	public void setCategory(String category)
	{
		this.category = category; 
	}
	

}
