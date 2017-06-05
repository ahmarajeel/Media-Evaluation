/* Author: Ahmar Mohammed
 * Date: 05/15/2017 
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;


/**
 *  The purpose of this class is to have a storage. I am using Hashtable for this purposes.
 *  I am inserting, deleting, modifying and reporting in this class.  
 */

public class Storage 
{
    // declaring hashtable
    Hashtable<String, Media> table = new Hashtable<>(); 
    String output_file; // for outputting 

    // storage constructor that takes output file as argument
    public Storage(String out)
    {
        output_file=out;
        try
        {
            new FileWriter(new File(output_file),false);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    // default constructor that does nothing
    public Storage() 
    {
    	
    }
    
    //insert method
    public void insert(String str, Media val)
    {
        logChanges("Inserting ", str, val);
        table.put(str, val);
    }
    
    public void modify(String str, Media val)
    {
        logChanges("Modifying ", str, val);
        table.replace(str, val);
    }
    
    public void delete(String str)
    {
        logChanges("Deleting ", str, table.get(str));
        table.remove(str);
    }

    // Checking if the hash table is empty
    public boolean isEmpty() 
    {
        return table.isEmpty();
    }
    
    // this method keep track of log and all the command customer did
    public void logChanges(String command, String str, Media med)
    {
        try
        {
            FileWriter logFile = new FileWriter(new File(output_file),true);
            Date date = new Date();

            logFile.write(command);
            logFile.write("at " + date);
            logFile.write(System.lineSeparator());
            logFile.write(med.getIsbn() + "|"+ med.getTitle()+"|" + med.getAuthor() + "|" + med.getPublisher() + "|" + med.getYear_published() +
            	"|" + med.getMinPrice() + "|" + med.getMaxPrice()+"|" + med.getAvgPrice()+"|" + med.getQuantity()+"|" + med.getItemCondition());
            
            logFile.write(System.lineSeparator());
            logFile.write(System.lineSeparator());
            logFile.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    // this is called when report button is pressed in GUI. 
    public void report(String filename)
    {
        if(!filename.endsWith(".txt"))
        {	
        	filename = filename+".txt";
        }
        try 
        {
            // Going through all the entires and saving it them to the file.
        	FileWriter outputFile = new FileWriter(new File(filename),false);
            outputFile.write("ISBN | Title | Author | Publisher | YearPublished | minPrice | maxPrice | AveragePrice | Quantity| Item Condition");
            Enumeration<String> keys= table.keys();
            String key;
            while(keys.hasMoreElements())
            {
                key=keys.nextElement();
                Media i = table.get(key);
                outputFile.write(System.lineSeparator());
                outputFile.write(i.getIsbn() + " | " + i.getTitle() + " | "  + i.getAuthor()+" | " + i.getPublisher()+" | " + i.getYear_published() +
                		" | " +  i.getMinPrice()+ " | " + i.getMaxPrice() + " | "+i.getAvgPrice() + " | "+i.getQuantity()+" | " + i.getItemCondition());
            }
            outputFile.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

}
