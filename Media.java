/* Author: Ahmar Mohammed
 * Date: 05/15/2017 
 */

import javax.swing.*;
import java.io.*;

/**
 *  The main purpose of this Media class is to contain bibliograpic information of Media. 
 *  It also has minPrice, maxPrice and AveragePrice to give more information to user. 
 *  
 */
public class Media 
{
    // all the fields are bibliographic info
	private String isbn;
    private String author;
    private String year_published;
    private String publisher;
    private String title;
    private double minPrice;
    private double maxPrice;
    private double avgPrice;
    int quantity = 1;
    private String itemCondition;

    // This constructor take only ISBN
    public Media(String isbn) 
    {
        this.isbn = isbn;
    }

    // This constructor take all the info and create instance object. 
    public Media(String isbn, String author, String year_published, String publisher, 
    		String title, double minPrice, double maxPrice, double avgPrice, String itemCondition) 
    {
        this.isbn = isbn;
        this.author = author;
        this.year_published = year_published;
        this.publisher = publisher;
        this.title = title;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.avgPrice = avgPrice;
        this.itemCondition = itemCondition;
    }
    
    public Media() 
    {

    }

	public String getAuthor() 
	{
		return author;
	}

	public void setAuthor(String author) 
	{
		this.author = author;
	}

	public String getYear_published() 
	{
		return year_published;
	}

	public void setYear_published(String year_published) 
	{
		this.year_published = year_published;
	}

	public String getPublisher() 
	{
		return publisher;
	}

	public void setPublisher(String publisher) 
	{
		this.publisher = publisher;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public double getMinPrice() 
	{
		return minPrice;
	}

	public void setMinPrice(double minPrice) 
	{
		this.minPrice = minPrice;
	}

	public double getMaxPrice() 
	{
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) 
	{
		this.maxPrice = maxPrice;
	}

	public double getAvgPrice() 
	{
		return avgPrice;
	}

	public void setAvgPrice(double avgPrice) 
	{
		this.avgPrice = avgPrice;
	}

	public int getQuantity() 
	{
		return quantity;
	}

	public void setQuantity(int quantity) 
	{
		this.quantity = quantity;
	}

	public String getItemCondition() 
	{
		return itemCondition;
	}

	public void setItemCondition(String itemCondition) 
	{
		this.itemCondition = itemCondition;
	}

	public void setIsbn(String isbn) 
	{
		this.isbn = isbn;
	}
	
	public String getIsbn() 
	{
	    return isbn;
	}
}

