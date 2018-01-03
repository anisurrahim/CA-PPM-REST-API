package com.clarity.rest.api;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.stream.Collectors;
import com.niku.union.utility.Base64;
import org.json.*;

public class ReadProjectsData {
    //Use a Clarity User Account having access to the project with project internal ID = 5020001
    private static String username = "admin";
    private static String password = "!Clarity152";
    //Clarity REST API URL to get single project data
   // private static String urlREST = "http://localhost/ppm/rest/v1/projects";
    private static String urlREST = "http://localhost/ppm/rest/v1/projects?isActive=true&createdDate=[2016-09-28T20:40:58,]&fields=createdDate,code,objective,name,isActive,costType&limit=100&offset=20";
    
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
                    System.out.println ("ProjectsData=" + fileData);
                    br.close();
                    
                    JSONObject json_data = new JSONObject(fileData);
                    String results = json_data.getString("_results");
                    JSONArray prjs_data = new JSONArray(results);

                    System.out.println ("ProjectsData Size = " + prjs_data.length());
                    for (int i = 0; i < prjs_data.length(); i++) {
                    	JSONObject  prj_data = prjs_data.getJSONObject(i);

                        String int_id= prj_data.getString("_internalId");
                        String code= prj_data.getString("code");
                        String name= prj_data.getString("name");
                        String self= prj_data.getString("_self");
                        String isActive= prj_data.getString("isActive");
                        String createdDate= prj_data.getString("createdDate");
                        System.out.println(i+ " ID="+int_id +", code="+code+", name="+name +", isActive="+isActive+", costType="+prj_data.getJSONObject("costType").getString("id")+", createdDate="+createdDate);
                        
                        URL url1 = new URL(urlREST+"/"+int_id);
                        HttpURLConnection conn1 = (HttpURLConnection) new URL(self).openConnection();
                       // HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
                        //HTTP Request Header
                        conn1.setDoInput(true);
                        conn1.setRequestMethod("GET");
                        conn1.setRequestProperty("Content-type", "application/json");
                        conn1.setRequestProperty("Accept", "application/json");
                        conn1.setRequestProperty("cache-control", "no-cache");
                        conn1.setRequestProperty("Authorization", basicAuth);
                        //Send HTTP GET Request
                        conn1.connect();
                        
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(
                                (conn1.getInputStream())));
                           
                           String prjData = br1.readLine();
                           System.out.println ("ProjData=" + prjData);
                           br1.close();
                           
                           JSONObject prjdata = new JSONObject(prjData);
                           JSONObject statusdata = new JSONObject(prjdata.getString("status"));                           
                           String obj = prjdata.getString("objective");
                           
                           System.out.println("Status="+statusdata.getString("displayValue") +", Objective="+obj);
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