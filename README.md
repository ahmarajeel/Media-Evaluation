# Media-Evaluation

Ahmar Mohammed
05/15/2017
Media Evaluation Instructions

The program can be run in following two ways:
1.	By giving two command line arguments namely, input.txt and output.txt
Note: input.txt can only contain ISBN
Following command can be used to run the program from command line
Compile: javac Driver.java
Run: java Driver input.txt output.txt
Once you run the program after all the insertion, GUI will pop showing minPrice, maxPrice and AveragePrice of all the book you have in the inventory. 

2.	If you don’t provide command line arguments, then program asks the following questions:

 

You can press yes to continue to GUI. If you press “no” then program exits and lets you provide command line arguments. 

When you press yes, you will see the following window:

 

Here we have 5 buttons. We will discuss each button following:

1.	Insert: As the name suggests with insert button, we can provide an ISBN and the program will add this ISBN if it is valid
2.	Delete All: This button should be used carefully as it will delete everything in the storage. 
3.	Delete: This button deletes an entry from storage if it already present. 
4.	Insert Form File: This option lets you choose file from file choose window. It looks like following: 
 

5.	Report: This button will ask you to give a friendly name to your report file and saves the current state of database to that file in the current directory. 
After successfully inserting ISBNs, the GUI should look like following window:
 


