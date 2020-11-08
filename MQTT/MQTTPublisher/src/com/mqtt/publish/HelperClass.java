package com.mqtt.publish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HelperClass {

	 public static String getFullStringResponse(HttpURLConnection con) throws IOException {
         
         // read response content
         BufferedReader in = null;
         if (con.getResponseCode() > 299) {
             in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
         } else {
             in = new BufferedReader(new InputStreamReader(con.getInputStream()));
         }
         String inputLine;
         StringBuffer content = new StringBuffer();
         while ((inputLine = in.readLine()) != null) {
             content.append(inputLine);
         }
         in.close();

         return  content.toString();
     }
	 
	public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}
	 
    public static List<Long> getFullLongResponse(HttpURLConnection con) throws IOException {
        
        // read response content
        BufferedReader in = null;
        if (con.getResponseCode() > 299) {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        String s =content.toString();
        
        String test2 = s.replaceAll("\\[", "");
        String test3 = test2.replaceAll("\\]", "");
        String test4 = test3.replaceAll("\\s", "");
        String[] array = test4.split(",");
        
        List<Long> ids = new ArrayList<>();
        
        for (String s2 : array) {
        	ids.add(Long.parseLong(s2));
        }
        
        return ids;
        
    }
    
    public static String removeSpacesAndAddCamelcase(String string) {
    	int n = string.length();  
    	char[] s =string.toCharArray();
        int res_ind = 0;  
        int numspaces=0;
      
        for (int i = 0; i < n; i++) {  
        	  
            // check for spaces in the sentence  
            if (s[i] == ' ') {  
            	numspaces++;
                // conversion into upper case  
                s[i+1] = Character.toUpperCase(s[i + 1]);  
                continue;  
            }  
      
            // If not space, copy character  
            else
                s[res_ind++] = s[i];          
        }  
      
        // return string to main
        String result = new String(s);
        String result2 =result.substring(0, result.length()-numspaces);
        return result2;  
    }  
	    

	 
	 
}
