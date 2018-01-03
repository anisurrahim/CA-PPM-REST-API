package com.clarity.rest.api;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.stream.Collectors;
import com.niku.union.utility.Base64;
import org.json.*;

public class WriteProject {
     //Use a Clarity User Account having access to Clarity and rights to create project
     private static String username = "admin";
    private static String password = "!Clarity152";
    //Clarity REST API URL to get single project data
    private static String urlREST = "http://localhost/ppm/rest/v1/projects";
     
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
               conn.setDoOutput(true);
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Content-type", "application/json");
               conn.setRequestProperty("Accept", "application/json");
               conn.setRequestProperty("cache-control", "no-cache");
               conn.setRequestProperty("Authorization", basicAuth);
               
               //Create Project write request in JSON format
               JSONObject jsonObj = new JSONObject();
               
               jsonObj.put("code", "PROJ0001");
               jsonObj.put("name", "Sample Test Project 1 - Created using REST API");
               jsonObj.put("description", "This a Test Project Created using REST API");
               jsonObj.put("scheduleStart", "2017-08-01T08:00:00");
               jsonObj.put("scheduleFinish", "2017-08-31T17:00:00");
               
               //Send HTTP POST Request
               OutputStreamWriter osw = new OutputStreamWriter(
                       (conn.getOutputStream()));                  
               osw.write(jsonObj.toString());               
               osw.close();
               //Check HTTP Response Code for success
               if (conn.getResponseCode() != 200) {
                    System.out.println("Failed : HTTP error code : " + conn.getHeaderField(0));
               } else {                    
                    //Process HTTP Response from Clarity REST API
                   BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                   
                   String outputData = br.readLine();
                   System.out.println ("Project Write Output=" + outputData);
                   br.close();
                   
                   //Process REST API response data in JSON format
                   JSONObject jsonOut = new JSONObject(outputData);
                   
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