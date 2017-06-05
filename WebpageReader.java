/* Author: Ahmar Mohammed
 * Date: 05/15/2017 
 */

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * This is very important class because all the parsing happens 
 * here. It reads the webpage, parse the useful information and 
 * add it to the database. Then storage takes it from there and
 * saves it. FInally GUI shows the content in a nice format.  
 */

public class WebpageReader 
{
    // code provided by Professor
     private static String webpage = "https://www.abebooks.com/servlet/SearchResults?isbn=";
     public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2) Gecko/20100115 Firefox/3.6";
    
     private static String ISBN = "";
     private static String withISBN = "";

     
   // constructor that takes ISBN as argument
    public WebpageReader(String isbnGiven) 
    {
        ISBN = isbnGiven;
        withISBN = webpage + isbnGiven; // this makes a valid URL so we can parse the information
    }
    
    // code provided by professor
    public static InputStream getURLInputStream(String sURL) throws Exception 
    {
        URLConnection oConnection = (new URL(sURL)).openConnection();
        oConnection.setRequestProperty("User-Agent", USER_AGENT);
        return oConnection.getInputStream();
    } 

    // code provided by professor
    public static BufferedReader read(String url) throws Exception 
    {
        InputStream content = (InputStream) getURLInputStream(url);
        return new BufferedReader(new InputStreamReader(content));
    } 

    /* All the data mining happens in this method
     * It gets the ISBN, gets the URL and concatenate with ISBN to get 
     * the media url. Then it uses regEX to get the information needed 
     * and insert into storage. 
     */
    public static void dataMine(Storage database) throws Exception 
    {
        String title = "";
        String author = "";
        double price = 0.0;
        String yearPublished = "";
        String publisher = "";
        String itemCondition = "";
    	
        // these variable are used to stop searching once found
        boolean publisherFound = false;
    	boolean yearFound = false;
    	boolean itemConditionFound = false;
    	
        URL url = new URL(withISBN);
        BufferedReader br = read(withISBN);

        String line1 = br.readLine();
        
        // Regular expression pattern saved into following variables
        String titleAuthorPattern = "(title>)" + ISBN + "( - )(.*)(by)(.*)( - AbeBooks)";
        String pricePattern = "(meta itemprop=)(\")(price)(\")( content=)(\")(.*?)(\")";
        String publisherPattern = "(<meta itemprop=)(\")(publisher)(\")( content=)(\")(.*?)(\")";
        String yearPattern ="(<meta itemprop=)(\")(datePublished)(\")( content=)(\")(.*?)(\")";
        String itemConditionPattern = "(<meta itemprop=)(\")(itemCondition)(\")( content=)(\")(.*?)(\")";
        
        // Here I am compiling the patterns
        Pattern titleAuthorReg = Pattern.compile(titleAuthorPattern);
        Pattern priceReg = Pattern.compile(pricePattern);
        Pattern publisherReg = Pattern.compile(publisherPattern);
        Pattern yearReg = Pattern.compile(yearPattern);
        Pattern itemConditionReg = Pattern.compile(itemConditionPattern);
        
        
        Matcher matcher;
        // creating arrayList to keep track of all the prices 
        ArrayList<Double> priceAry = new ArrayList<Double>();
        // In this while loop I am matching the patter line by line. 
        while(line1 !=null) 
        {
        	matcher = titleAuthorReg.matcher(line1);
        	if(matcher.find()) 
        	{
        		title = matcher.group(3);
        		author = matcher.group(5);
        	}
        	
        	matcher = priceReg.matcher(line1);
        	if(matcher.find()) 
        	{
        		priceAry.add(Double.parseDouble(matcher.group(7)));
        		//System.out.println(matcher.group(7));
        	}
        	
        	matcher = publisherReg.matcher(line1);
        	if(matcher.find() && !publisherFound) 
        	{
        		publisher = matcher.group(7);
        		publisherFound = true;
        	}
        	
        	matcher = yearReg.matcher(line1);
        	if(matcher.find() && !yearFound) 
        	{
        		yearPublished = matcher.group(7);
        		yearFound = true;
        	}
        	
        	matcher = itemConditionReg.matcher(line1);
        	if(matcher.find() && !itemConditionFound) 
        	{
        		itemCondition = matcher.group(7);
        		itemConditionFound = true;
        	}
        	line1 = br.readLine();
        }
        
        // price calculation for max, min and average
        double maxPrice = 0.0;
        double minPrice = 10000.00;
        double avgPrice = 0.0;
        double sum = 0.0;
        for(int i = 0; i < priceAry.size(); i++) 
        {
        	if(priceAry.get(i) < minPrice) minPrice = priceAry.get(i);
        	if(priceAry.get(i) > maxPrice) maxPrice = priceAry.get(i);
        	sum += priceAry.get(i);
        }
        avgPrice = sum/priceAry.size();
        avgPrice = Math.round(avgPrice*100.00)/100.00;
        price = minPrice;
               
        // Now putting the information into storage. 
        if(database.table.containsKey(ISBN))
        {
        	if(minPrice == 10000.00) 
        	{
            	//System.out.println("I am in 100000");
            	JOptionPane.showMessageDialog(null, "That is not a valid ISBN.");
            }
        	database.modify((ISBN), new Media(ISBN, author, yearPublished, publisher, title, minPrice, maxPrice, avgPrice, itemCondition));
        }
        else if(minPrice == 10000.00)
        {
        	//System.out.println("I am in 100000");
        	JOptionPane.showMessageDialog(null, "That is not a valid ISBN.");
        }
        else 
        {
            database.insert((ISBN), new Media(ISBN, author, yearPublished, publisher, title, minPrice, maxPrice, avgPrice, itemCondition));
        }
    }
}
