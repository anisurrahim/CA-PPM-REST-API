package com.clarity.rest.api;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.stream.Collectors;
import com.niku.union.utility.Base64;
import org.json.*;

public class ReadProjectTaskData {
     //Use a Clarity User Account having access to the project with project internal ID = 5020001
     private static String username = "admin";
    private static String password = "!Clarity152";
    //Clarity REST API URL to get single project data
    private static String urlREST = "http://localhost/ppm/rest/v1/projects/5003000?expand=(tasks=(fields=(code,startDate,costType)),teams)";
     
    public static void main(String[] args) {
          
          try {
               // Construct Base 64 Authenticate String
               String b64 = username + ":" + password;
               String basicAuth = "Basic " + Base64.encode(b64);
               System.out.println ("basicAuth=" + basicAuth);
               // Connection URL
               URL url = new URL(urlREST);
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               //HTTP Request Header
               conn.setDoInput(true);
               conn.setRequestMethod("GET");
               conn.setRequestProperty("Content-type", "application/json");
               conn.setRequestProperty("Accept", "application/json");
               conn.setRequestProperty("cache-control", "no-cache");
               conn.setRequestProperty("Authorization", basicAuth);
               //Send HTTP GET Request
               conn.connect();
               //Check HTTP Response Code for success
               if (conn.getResponseCode() != 200) {
                    System.out.println("Failed : HTTP error code : " + conn.getHeaderField(0));
               } else {
                    //Process HTTP Response from Clarity REST API
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                         (conn.getInputStream())));
                    
                    String fileData = br.readLine();
                    System.out.println ("ProjectData=" + fileData);
                    br.close();
                    
                    //Process REST API response data in JSON format
                    JSONObject jsonObj = new JSONObject(fileData);
                    
                    //Extract Name Vaue pairs from JSON object
                    for(int i = 0; i< jsonObj.names().length(); i++){
                         System.out.println ("Key = " + jsonObj.names().getString(i) + " | Value = " + jsonObj.get(jsonObj.names().getString(i)));
                    }
               }
          } catch (MalformedURLException e) {     
               e.printStackTrace();          
          } catch (IOException e) {
               e.printStackTrace();
               
          }catch (Exception e){//Catch exception if any
                System.err.println("Error: " + e.getMessage());
     
          }
               
     }

}