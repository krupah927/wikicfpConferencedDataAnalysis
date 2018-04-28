/************************************************************************
 * Author: Krupa Hegde
 * Student #: 862006278
 * CS235 Assignment phase 1
 ************************************************************************/

import java.net.*;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;


public class WikiCFPScraperTemplate {
	public static int DELAY = 7;
	public static void main(String[] args) {
	
		try {
				//String array containing categories to be crawled
				String[] categoryStr= {"data mining","databases","machine learning","artificial intelligence"};
			
				// Number of pages to crawl
				int numOfPages = 20;
	        
				//create the output file
				File file = new File("wikicfp_crawl_beforeClean.txt");
				file.createNewFile();
				PrintWriter writer = new PrintWriter(file); 
	       
				// rem variable is used in the later part of the code to store the remainder 
				int rem;
			
				//Lists to store the crawled data
				ArrayList<String> output1 = new ArrayList<String>(); 
				ArrayList<String> output2 = new ArrayList<String>(); 
				ArrayList<String> output3 = new ArrayList<String>(); 
	        
				//loop to crawl data for all the categories given
				for(int category=0;category<categoryStr.length;category++) {
	        	
	        
					//now start crawling the all 'numOfPages' pages
					for(int page = 1;page<=numOfPages;page++) {
					
						// link to crawl the data 
						String linkToScrape = "http://www.wikicfp.com/cfp/call?conference="+
	        				      URLEncoder.encode(categoryStr[category], "UTF-8") +"&page=" + page;
	            	
						// Jsoup.connect(url) creates a new connection 
						//get() fetches and parses HTML response
						Document document = Jsoup.connect(linkToScrape).get();


						//selecting table from the page
						//required data is in the 5th table in the website 
						//Jsoup select selects the required Html content and get() fetches and parses data
						Element tableData = document.select("table").get(5); 
						//to select the rows of the table
						Elements rows = tableData.select("tr");

						// counter variable to be used to get the data from required row, it holds row number
						int i=0;
	  	   
						//loop to iterate through all the rows. 
						//For every row in rows td tag in html inside the table is selected
						for (Element row : rows) {
							Elements tdData = row.select("td");
							
							//Fetch data for only those rows which have more than one td in a row
							if(tdData.size()>1) {
								//ignore the 0th row
								if(i==0) {
									i++;
									continue;
								}
								
								//The website contains data in 2 rows format the first row for each conference contains Acronym and the second row contains location of conference
								rem=i%2; 
								
								if (rem==0) {
									// fetch location and add it to the list containing locations
									output3.add(tdData.get(1).text()); 
								}
	  	    	  
								else{
									//fetch acronym data and conference name data and add them in respective lists
									output1.add(tdData.get(0).text()); //acronym
									output2.add(tdData.get(1).text()); // Conference name
								} 
								i++; 
							}
						}
	  	   
	  		
	        		        	
						//rate-limit the queries
						Thread.sleep(DELAY*1000); 
					}
	        
				} 
				//Add Column names to  output file
				writer.println("Acronym \t"+"Conference name \t"+"Location");
				
				//m is an iterator variable, since size of all 3 lists are same we can consider any one of them
				//loop through the list to write data to file.
				int m=output1.size();
				for(int k=0;k<m;k++) {
					//println of PrintWriter class writes in new line everytime.
		 		     writer.println(output1.get(k)+"\t "+output2.get(k)+"\t"+output3.get(k));
				}
				
				//close the file after writing data into the file
				writer.close();
				
				} catch (IOException e) {  //to handle exceptions try catches are used
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

	}
	
}
