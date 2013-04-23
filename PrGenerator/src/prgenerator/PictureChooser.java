/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author Yuliya Kuznetsova
 */
public class PictureChooser {
                
    private final String START_URL="https://ajax.googleapis.com/ajax/services/search/images?v=1.0";   //start of image search url
    private LinkedList<String> helper = new <String>LinkedList();  //helper list for temporary pictures collection
     
    /*
     * special parameters of  image search url:
     * &rsz - number of images to be found. Allowed Values: 1 - 8
     * &imgsz - image size (icon|small|medium|large|xlarge|xxlarge|huge)
     * &as_filetype -  image search to the specific file types (jpg|gif|png|bmp)
     * &userip - supplies the IP address of the end-user on whose behalf the request is being made  
     *  in order to indetify the user and avoid a violation of the Terms of Service of the Google Image Search API.
     * &hl -  host language of the application making the request
     */
    private final String PARAMETERS_URL="&rsz=8&imgsz=medium&as_filetype=jpg&userip=192.168.0.1&hl=de&q=";
    
    /**
     * Method for choosing a picture for the application,
     * invokes sendRequests() and then chooses randomly
     * a picture from a created pictures list
     * 
     */
    public void choosePicture() {
    
     //System.out.println("Picture list before choose picture: " +PrGenerator.mainDatabase.getPictureList()); 
       
      helper=new <String>LinkedList(); 
          
      System.out.println( "\n" + "hier: UserInputFiltered is Empty: " +PrGenerator.mainDatabase.getUserInputFiltered().isEmpty());
      
      //google search only if user input exists
      
      if(!PrGenerator.mainDatabase.getUserInputFiltered().isEmpty()){
         
      // System.out.println("configure requests");
       configureRequests();
       //System.out.println("Picture list after configure requests: " +PrGenerator.mainDatabase.getPictureList()); 
        }         
      
       //select random picture from a pictureList
       int randomNumber = (int) (Math.random() * (PrGenerator.mainDatabase.getPictureList().size()));
       String choosenPicture = PrGenerator.mainDatabase.getPictureList().get(randomNumber);
       PrGenerator.mainDatabase.setChosenPicture(choosenPicture);
            
                    
           
       System.out.println("pictureList after choosePicture():  ");
             for (Iterator<String> i = PrGenerator.mainDatabase.getPictureList().iterator(); i.hasNext();) {
                String s = i.next();
                System.out.println(s);
            }
            
            System.out.println("\n" + "Choosen Picture is " + PrGenerator.mainDatabase.getChosenPicture());
        
    }
    
    /**
     * method which configures search parameter for google image search api,
     * key search parameters are: either the whole heading or user input and main words from heading of a document.
     * If no image for the whole heading is found, try to find images for user input and|or main word from a heading.
     * invokes findPictures (String adress) 
     * 
     */
    private void configureRequests() {

         
        LinkedList<String> userInputFiltered = PrGenerator.mainDatabase.getUserInputFiltered(); //filtered user input
        String inputAsString = "";
        String url;
        System.out.println("Filtered user input " +PrGenerator.mainDatabase.getUserInputFiltered());
        System.out.println("User input: " +PrGenerator.mainDatabase.getUserInput());
                       
        // build string of the entire userinput
        for (Iterator<String> i = userInputFiltered.iterator(); i.hasNext();) {
            
            inputAsString = i.next() +" " +inputAsString ;
        }
        System.out.println("inputAsStr:" +inputAsString);
         
        url=START_URL + PARAMETERS_URL +inputAsString.replace(" ", "+");
       // url=START_URL + PARAMETERS_URL +PrGenerator.mainDatabase.getUserInput().replace(" ", "+"); //first searching for the url
        
        boolean foundFullInput=findPictures(url, true); //first searches for the entire url
        
        System.out.println("foundFullInput? " +foundFullInput);
         
        if(!foundFullInput) {
            //if no image for the entire intput is found search for the particular part of the unput
            while (!userInputFiltered.isEmpty()) {
               url = START_URL + PARAMETERS_URL + userInputFiltered.getFirst().replace(" ", "+"); // replace blanks in the user input to get a proper url 
               System.out.println("current address if not found full input " + url);
                findPictures(url, false); 
               // findPictures("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=1&imgsz=big&as_filetype=jpg&userip=192.168.0.1&hl=de&q=banane");
               userInputFiltered.removeFirst();
           }
        }
        
         //System.out.println("Helper empty?" +helper.isEmpty());
        
         //update picture list if picture is found
         if(!helper.isEmpty()){
        System.out.println("helper" + helper);
        PrGenerator.mainDatabase.setPictureList(helper); // set up the pictureList in the database with founded pictures
        System.out.println("Picture list after choose picture: " +PrGenerator.mainDatabase.getPictureList()); 
         }
         
         System.out.println("Picture list after choose picture: " +PrGenerator.mainDatabase.getPictureList());
    }
    
    /**
     * method that searches for the pictures online with help of Google Image Search API
     * and adds founded pictures to the database in a pictureList.
     * For each request one image url will be  selected.
     * Uses JSON to get the Web-Content
     * @return true if a picture for heading could be founded, false otherwise
     * @param adress - url for image search
     * @param UserInput - true if picture for the entire user input should be found
     */
    
    private boolean findPictures(String address, boolean UserInput) {
        //open url, establish connection and read content
        try {
           // System.out.println("Picture list in findPictures at the beginning: " +PrGenerator.mainDatabase.getPictureList()); 
            URL url = new URL(address);
            System.out.println("Url to search: " +url);
            URLConnection connection = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close(); //added by Dawid
            
            //System.out.println("after reader.close()");
            JSONObject json = new JSONObject(builder.toString());  // construct a JSONObject from page content
            System.out.println("after json is constructed");
            if (json.getJSONObject("responseData").getJSONArray("results").length()==0){
                System.out.println("if teil");
                System.out.println("no pics found");
                System.out.println("Picture List if no picture found" +PrGenerator.mainDatabase.getPictureList());
                return false;
            }
            else{
                System.out.println("found smth");
                int randNumb=(int)(Math.random() * 6);
            String imageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(0).getString("unescapedUrl"); //get the url-property of a json object           
            //String tbImageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(0).getString("tbUrl");
            
            System.out.println("founded imageUrl" +imageUrl);
            helper.add(imageUrl);   // add  founded picture to helper list
            System.out.println("helper" +helper);
            if (UserInput == true) {
                return true;
            } else {
                return false;
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception, false is returned");
           return false;
        }

        /* other api 
         * URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyAoFTQH58kb6GEF-o0v7qKh5kMqPmZl5oo&cx=006298392676923811362:bsx_ivypxlm&q=dhbw&searchType=image");
         * String imageUrl = json.getJSONArray("items").getJSONObject(i).getString("link"); 
         */
    }
}
