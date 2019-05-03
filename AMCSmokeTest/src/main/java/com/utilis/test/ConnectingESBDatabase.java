package com.utilis.test;
import java.util.ArrayList;

import com.cmstestbase.test.CMSTestBase;
public class ConnectingESBDatabase extends CMSTestBase {

	public String errorText;
	public String eventType;
	public  ArrayList<String> rowData = new ArrayList<String>();
	boolean flag = true;
	
	//Connecting to ESB Database

	public void connectESBDatabase() throws Throwable {
		
		CMSTestBase CMTB = new CMSTestBase();
		
		CMTB.ConnectDBandExecuteESBQuery(1);

	}

	public void hierarchyCreation(String ExpectedEvents) {

		for (int i = 0; i <= CMSTestBase.ActualEvents.size()-1 ; i++) {
			
			//System.out.println(CMSTestBase.ActualEvents.size()-1);
			
			String rowdata =  CMSTestBase.ActualEvents.get(i);

			if(rowdata.contains(ExpectedEvents) && (!rowdata.contains("Exception")) &&!rowdata.contains(".ACK") ) {
				
				flag = false;
				
				eventType="event found";
				
				//System.out.println("Expected :" + ExpectedEvents + "---->" + "Actual :" + rowdata);
				
				rowdata =rowdata.replace("[", "").replace("]"," ").replace(",", " ");
				
				String[] words = rowdata.split(" ");
				
				
				for(int j =0; j<=words.length-1 ; j++) {
					
					if(ExpectedEvents.equalsIgnoreCase(words[j])) {
						
						logStep("Expected :" + ExpectedEvents);
						
						logStep("Actual :" + words[j]);
						
						System.out.println("Expected :" + ExpectedEvents + "---->" + "Actual :" + words[j]);
						break;
					} 
				} 
				

			}else if (rowdata.contains(ExpectedEvents) && (!rowdata.contains("Exception")) && rowdata.contains(".ACK")){
				
				flag = false;
				
				eventType="event found";

				String errormsg = "Expected :" + ExpectedEvents + "---->" + "Actual -- But found error inthe DB";

				System.out.println("Error msg found : - "+errormsg);
				
                 rowdata =rowdata.replaceAll("[", "");
				
				String[] words = rowdata.split(" ");
				
				for(int k =0; k<words.length-1 ; k++) {
					
					if(ExpectedEvents.equalsIgnoreCase(words[k])) {
						
						logStep("Expected :" + ExpectedEvents + "---->" + "Actual :" + words[k]);
						
						System.out.println("Expected :" + ExpectedEvents + "---->" + "Actual :" + words[k]);
					    
						break; 
						 
					} 				
					
			} 
			
		}
		if(flag) {
				eventType="event not found";
			}
		}

	        	
	        }
	
	
}
	
	
	
	

