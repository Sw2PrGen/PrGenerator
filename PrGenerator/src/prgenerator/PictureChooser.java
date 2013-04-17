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
    private LinkedList<String> helper;  //helper list for temporary pictures collection
     
    /*
     * special parameters of  image search url:
     * &rsz - number of images to be found. Allowed Values: 1 - 8
     * &imgsz - image size (icon|small|medium|large|xlarge|xxlarge|huge)
     * &as_filetype -  image search to the specific file types (jpg|gif|png|bmp)
     * &userip - supplies the IP address of the end-user on whose behalf the request is being made  
     *  in order to indetify the user and avoid a violation of the Terms of Service of the Google Image Search API.
     * &hl -  host language of the application making the request
     */
    private final String PARAMETERS_URL="&rsz=1&imgsz=medium&as_filetype=jpg&userip=192.168.0.1&hl=de&q=";
    
    /**
     * Method for choosing a picture for the application,
     * invokes sendRequests() and then chooses randomly
     * a picture from a created pictures list
     * 
     */
    public void choosePicture() {
        helper = new <String>LinkedList(); 
       System.out.println( "\n" + "hier: UserInputFiltered is Empty: " +PrGenerator.mainDatabase.getUserInputFiltered().isEmpty());
      //!!!! if(!PrGenerator.mainDatabase.getUserInputFiltered().isEmpty()){
         if(PrGenerator.mainDatabase.getUserInputFiltered().isEmpty()){
             System.out.println("configure requests");
       configureRequests();
       
       }         
      
        //select random picture from a pictureList
        int randomNumber = (int) (Math.random() * (PrGenerator.mainDatabase.getPictureList().size()));
            String choosenPicture = PrGenerator.mainDatabase.getPictureList().get(randomNumber);
            PrGenerator.mainDatabase.setChosenPicture(choosenPicture);
            
            //show choosen picture just for current testing         
           
            System.out.println("pictureList: ");
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

         
        LinkedList<String> userInput = PrGenerator.mainDatabase.getUserInputFiltered(); //filtered user input
       // System.out.println("Filtered user input" +PrGenerator.mainDatabase.getUserInputFiltered());
        
        String url;            
       
             
            //while (!userInput.isEmpty()) {
               // url = START_URL + PARAMETERS_URL + userInput.getFirst().replace(" ", "+"); // replace blanks in the user input to get a proper url 
                //System.out.println("current address " + url);
              //  findPictures(url); 
                findPictures("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=1&imgsz=big&as_filetype=jpg&userip=192.168.0.1&hl=de&q=banane");
             //   userInput.removeFirst();
           // }
         
         if(!helper.isEmpty()){
        
        PrGenerator.mainDatabase.setPictureList(helper); // set up the pictureList in the database with founded pictures
         }
    }
    
    /**
     * method that searches for the pictures online with help of Google Image Search API
     * and adds founded pictures to the database in a pictureList.
     * For each request one image url will be  selected.
     * Uses JSON to get the Web-Content
     * @return true if a picture for heading could be founded, false otherwise
     * @param adress - url for image search
     * @param heading - true if picture for the whole heading should be found
     */
    private void findPictures(String address) {
        //open url, establish connection and read content
        try {
            URL url = new URL(address);
            System.out.println("Url: " +url);
            URLConnection connection = url.openConnection();

            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            
            reader.close(); //added by Dawid
            
            JSONObject json = new JSONObject(builder.toString());  // construct a JSONObject from page content
            if (json.getJSONObject("responseData").getJSONArray("results").length()==0){
                System.out.println("no pics found");
            }
            else{
            String imageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(0).getString("unescapedUrl"); //get the url-property of a json object           
            String tbImageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(0).getString("tbUrl");
            /*abfrage 
             * der bildergröße
             * wenn zu groß -> tbUrl nehmen
             */
            System.out.println("imageUrl" +imageUrl);
            helper.add(imageUrl);   // add  founded picture to helper list
            System.out.println("helper" +helper);}
            
        } catch (Exception e) {
            //e.printStackTrace();
           
        }

        /* other api 
         * URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyAoFTQH58kb6GEF-o0v7qKh5kMqPmZl5oo&cx=006298392676923811362:bsx_ivypxlm&q=dhbw&searchType=image");
         * String imageUrl = json.getJSONArray("items").getJSONObject(i).getString("link"); 
         */
    }
}
