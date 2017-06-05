/* Author: Ahmar Mohammed
 * Date: 05/15/2017 
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.sound.midi.SysexMessage;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import java.awt.TextArea;

/**
 *  The main purpose of this class is to setup Graphical User Interface for the user
 *  so it is easier for the user to navigate through
 */

public class GraphicalUserInterface extends Thread
{
	
	JFrame frmAbebookscom;   // This is the main frame
							// prices and ISBN
	
	Storage storage;  // this is used to store data
	WebpageReader url; // this is used in datamining
	private JTable table; 
	TextArea textArea;
	// to display the inserts

	/**
	 *  This constructor takes two parameters, storage for data and url for datamining
	 */
	public GraphicalUserInterface(Storage storage, WebpageReader url) 
	{
		this.storage = storage;
		this.url = url;
		initialize();  // to start the GUI
		showContent(); // Show content in the frame	
	}

	/**
	 *  If the valid ISBN is given, it will be inserted and updated content will be showed 
	 */
	public void insert() throws Exception 
	{
		/*
		 * Here, I am handling if ISBN is already exists in the database. If it does, 
		 * then I am increasing quantity.
		*/ 
       
		String id = (JOptionPane.showInputDialog("Input ISBN"));
        if(storage.table.containsKey(id))
        {
            JOptionPane.showMessageDialog(null, "ISBN already exists. So, quantity increase by 1");
        	storage.table.get(id).quantity++;
        }
        else 
        {
        	/*
    		 * If ISBN does not exists then look its information into abebooks.com and put it into the 
    		 * database.
    		*/ 
        	//storage.insert(id, new Media(id));
            WebpageReader url = new WebpageReader(id);
            url.dataMine(storage);
        }
        showContent();
	}
	
	/*
	 * If ISBN does not exists then look it say that to user. If it exist, it deletes it and update the content
	 * by using showContent(). 
	*/
	public void delete() 
	{
		String id = (JOptionPane.showInputDialog("Input ISBN"));
		if(!storage.table.containsKey(id)) 
		{
			 JOptionPane.showMessageDialog(null, "ISBN does not exists!");
		}
		else 
		{
			storage.delete(id);
			showContent();
			JOptionPane.showMessageDialog(null, "ISBN "+ id + " has been removed successfully.");
		}
		showContent();
	}
	
	/*
	 * This method delete all record in the database. But it will make before it does that.  
	*/
	public void deleteAll() throws InterruptedException 
	{
		if(storage.table.isEmpty()) 
		{
			JOptionPane.showMessageDialog(null, "Hash Table is already empty");
			textArea.setText("");
		}
		else
		{
			// Just making sure before deleting everything
			int num = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete everything!");
			if(num == 0) 
			{
				storage.table.clear();
				// showing nothing after deletion.
				textArea.setText("");
				JOptionPane.showMessageDialog(null, "Successfully deleted all entries.");
			}
			else 
			{
				return;
			}
		}	
	}
	/*
	 * This is a crucial method which is responsible for showing the content on the GUI
	 * It goes through all the keys in the database and shows relative prices.   
	*/
	public void showContent() 
	{
		System.out.println("I am in showContent!");
		if(storage.table.isEmpty()) 
		{
			// showing nothing if it is empty.
			textArea.setText("");
			return;
		}
		Set<String> keys = storage.table.keySet();
		String str = "";
		for(String key: keys) 
		{
			str += (storage.table.get(key).getIsbn() + "\t\t\t" + "$"+storage.table.get(key).getMinPrice() + 
					"\t\t\t\t" + "$"+storage.table.get(key).getMaxPrice() + "\t\t\t\t" + "$"+storage.table.get(key).getAvgPrice() + "\n");
		}
		textArea.setText(str);

	}
	
	/*
	 * This is an easier way to select a file that contains ISBNs and it will give you 
	 * minPrice, maxPrice and AveragePrice   
	*/
	public void insertFromFile() 
	{
		
		/*
		 *  This is a file selector. Very similar to when we browse to
		 *  select the file.
		 */  
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		File file = chooser.getSelectedFile();
		BufferedReader br;
		String line = "";
		
		try 
		{
            br = new BufferedReader(new FileReader(file));
            line = br.readLine();
            System.out.println("I am working..");
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
                url = new WebpageReader(line);
                url.dataMine(storage);
                // This is where we show the content one by one
                textArea.append((storage.table.get(line).getIsbn() + "\t\t\t" + "$"+storage.table.get(line).getMinPrice() + 
    					"\t\t\t\t" + "$"+storage.table.get(line).getMaxPrice() + "\t\t\t\t" + "$"+storage.table.get(line).getAvgPrice() + "\n"));
                
                // sleeping for smooth transition
        		try {
        			Thread.sleep(1000);
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
                line = br.readLine();
            }
            
            
        } 
		catch (Exception e) 
		{
            e.printStackTrace();
        }
		JOptionPane.showMessageDialog(null, "Successfully inserted all entries from the selected file.");
	}
	
	

	/*
	 *  This is gonna run when button is pressed. It will ask for the friendly name and save the report
	 */ 
	public void report() 
	{
		String str = JOptionPane.showInputDialog("Give a friendly name to your report.");
		storage.report(str);
		JOptionPane.showMessageDialog(null, "A report has been sucessfully created and saved in the current directory");
	}

	/*
	 *  This is where all the components are initialized and set the sizes
	 */ 
	private void initialize() 
	{
		// all GUI is setup here and initialized. 
		frmAbebookscom = new JFrame();
		frmAbebookscom.getContentPane().setEnabled(false);
		frmAbebookscom.setTitle("AbeBooks.com");
		frmAbebookscom.setBounds(100, 100, 800, 400);
		frmAbebookscom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAbebookscom.getContentPane().setLayout(null);

		JButton btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					insert();
				} 
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
		btnInsert.setBounds(52, 300, 89, 23);
		frmAbebookscom.getContentPane().add(btnInsert);
		
		/*
		 *  When delete all button is pressed. This code runs and goes to deleteAll method.
		 */
		JButton btnModify = new JButton("Delete All");
		btnModify.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					deleteAll();
				} 
				catch (InterruptedException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnModify.setBounds(200, 300, 89, 23);
		frmAbebookscom.getContentPane().add(btnModify);
		
		/*
		 *  When delete button is pressed. This code runs and goes to delete method.
		 */
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				delete();
			}
		});
		btnDelete.setBounds(493, 300, 89, 23);
		frmAbebookscom.getContentPane().add(btnDelete);
		
		/*
		 *  When report all button is pressed. This code runs and goes to report method.
		 */
		JButton btnReport = new JButton("Report");
		btnReport.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				report();
			}
		});
		btnReport.setBounds(626, 300, 89, 23);
		frmAbebookscom.getContentPane().add(btnReport);
		
		table = new JTable();
		table.setBounds(90, 31, 1, 1);
		frmAbebookscom.getContentPane().add(table);
		
		/*
		 *  Following is the code for all the buttons in the GUI
		 */
		JLabel lblIsbn = new JLabel("ISBN");
		lblIsbn.setBounds(52, 18, 46, 14);
		frmAbebookscom.getContentPane().add(lblIsbn);
		
		JLabel lblMinPrice = new JLabel("Min Price");
		lblMinPrice.setBounds(223, 18, 67, 14);
		frmAbebookscom.getContentPane().add(lblMinPrice);
		
		JLabel lblMaxPrice = new JLabel("Max Price");
		lblMaxPrice.setBounds(427, 18, 74, 14);
		frmAbebookscom.getContentPane().add(lblMaxPrice);
		
		JLabel lblAveragePrice = new JLabel("Average Price");
		lblAveragePrice.setBounds(605, 18, 110, 14);
		frmAbebookscom.getContentPane().add(lblAveragePrice);
		
		// Here is the code for Insert from file button and it calls the method
		JButton btnInsertFromFile = new JButton("Insert From File");
		btnInsertFromFile.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				insertFromFile();
			}
		});
		btnInsertFromFile.setBounds(324, 300, 131, 23);
		frmAbebookscom.getContentPane().add(btnInsertFromFile);
		
		textArea = new TextArea();
		textArea.setBounds(52, 38, 652, 256);
		frmAbebookscom.getContentPane().add(textArea);
	}
}
