/* Author: Ahmar Mohammed
 * Date: 05/15/2017 
 */

import java.awt.EventQueue;
import java.io.*;
import java.text.ParseException;
import javax.swing.JOptionPane;


/**
 *  The main purpose of this class is to start the program and sort of run as 
 *  the user wants it to run
 *  For example: If user does not provide command line arguments then GUI comes 
 *  and ask if he or she wants to continue with the GUI. If arguments are given
 *  then program goes through ISBN list and show the content in the GUI
 */
public class Driver  
{
       
    public static void main (String args[]) 
    {

        String log = "logFile.txt"; // to keep track of user commands
        WebpageReader url = null;   // to send ISBN to WebpageReader
        
        if(args.length == 2)  // if two arguments are given continue here otherwise go to GUI
        {
            System.out.println("Two files received!");
            System.out.println("I am working..");
            BufferedReader br;
            String line ="";
            Storage storage = new Storage(log); 
            
            if(!args[0].toString().contains(".txt"))
            {	
            	return; // if txt is not given exit the program
            }
            
            try 
            {
                br = new BufferedReader(new FileReader(args[0]));
                line = br.readLine();
                
                while(line!=null)
                {
                    if(storage.table.containsKey(line))
                    {
                        storage.table.get(line).quantity++;
                    }
                    else 
                    {
                        storage.insert(line, new Media(line));
                    }
                    
                    // now we have the ISBN so sending it to webpageReader for data mining
                    url = new WebpageReader(line);
                    url.dataMine(storage);
                    
                    line = br.readLine();
                    //System.out.println("Next Read..");
                }
                
                EventQueue.invokeLater(new Runnable() 
                {
        			public void run() 
        			{
        				try 
        				{
        					//Storage storage = new Storage(log); 
        					WebpageReader url = new WebpageReader(null);
        					GraphicalUserInterface window = new GraphicalUserInterface(storage, url);
        					window.frmAbebookscom.setVisible(true);
        				} 
        				catch (Exception e) 
        				{
        					e.printStackTrace();
        				}
        			}
        		}); 
                
                
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            
            storage.report(args[1]);
        }
        
        // here is else part where the GUI goes. This happens only if user didn't provide command line arguments
        else 
        {	
        	int confirm = JOptionPane.showConfirmDialog(null, "I see you didn't provide command line arguments! Would you like to continue with GUI");
        	if(confirm == 1) 
        	{
        		JOptionPane.showMessageDialog(null, "Try giving command line arguments. Good bye!");
        		System.exit(1);
        	}
        	else 
        	{
	        	EventQueue.invokeLater(new Runnable() 
	        	{
	    			public void run() 
	    			{
	    				try 
	    				{
	    					Storage storage = new Storage(log); 
	    					WebpageReader url = new WebpageReader(null);
	    					GraphicalUserInterface window = new GraphicalUserInterface(storage, url); // starting GUI
	    					window.frmAbebookscom.setVisible(true);
	    				} 
	    				catch (Exception e) 
	    				{
	    					e.printStackTrace();
	    				}
	    			}
	    		});
        	}
        
        }
    }

}
