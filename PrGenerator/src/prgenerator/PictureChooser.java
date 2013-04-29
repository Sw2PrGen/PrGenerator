/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import org.json.JSONObject;

/**
 * PictureChooser is responsible for picture search for the application.
 * Google Image Search API is used
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

        helper = new <String>LinkedList(); 

        //google search only if user input exists      
        if (!PrGenerator.mainDatabase.getUserInputFiltered().isEmpty()) {
            configureRequests();
        }

        //select random picture from a pictureList
        int randomNumber = (int) (Math.random() * (PrGenerator.mainDatabase.getPictureList().size()));
        String choosenPicture = PrGenerator.mainDatabase.getPictureList().get(randomNumber);
        PrGenerator.mainDatabase.setChosenPicture(choosenPicture); //write choosen picture in the database         

        /*    
         System.out.println("pictureList after choosePicture():  ");
         for (Iterator<String> i = PrGenerator.mainDatabase.getPictureList().iterator(); i.hasNext();) {
         String s = i.next();
         System.out.println(s);
         }
            
         System.out.println("\n" + "Choosen Picture is " + PrGenerator.mainDatabase.getChosenPicture());
         */
    }
    
    /**
     * method which configures search parameters for google image search api,
     * key search parameters are: either the entire  user input 
     * or (if nothing found) separated parts of user input - if there is more than one word.
     * invokes findPictures (String adress) 
     * 
     */
    private void configureRequests() {


        LinkedList<String> userInputFiltered = PrGenerator.mainDatabase.getUserInputFiltered(); //filtered user input
        String inputAsString = ""; //helper to build the entire user input as string
        String url; //url to search

        // build string of the entire userinput
        for (Iterator<String> i = userInputFiltered.iterator(); i.hasNext();) {

            inputAsString = i.next() + " " + inputAsString;
        }

        url = START_URL + PARAMETERS_URL + inputAsString.replace(" ", "+");

        boolean foundFullInput = findPictures(url, true); //first searches for the entire user input; 

        if (!foundFullInput) {
            //if no image for the entire user intput is found search for the particular words of the unput
            while (!userInputFiltered.isEmpty()) {
                url = START_URL + PARAMETERS_URL + userInputFiltered.getFirst().replace(" ", "+"); // replace blanks in the user input to get a proper url 
                findPictures(url, false);
                userInputFiltered.removeFirst();
            }
        }

        //update picture list if picture is found
        if (!helper.isEmpty()) {
            PrGenerator.mainDatabase.setPictureList(helper); // set up the pictureList in the database with founded pictures
        }
    }
    
    /**
     * method that searches for the pictures online with help of Google Image Search API
     * and adds founded pictures to the temporarily helper list.
     * For each request one random image url will be  selected.
     * Uses JSON to get the Web-Content
     * @return true if a picture for the entire user input could be founded, false in all other cases
     * @param adress - url for image search
     * @param UserInput - true if picture for the entire user input should be found, false otherwise
     */
    
    private boolean findPictures(String address, boolean UserInput) {
        //open url, establish connection and read content
        try {
            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();

            // construct a JSONObject from page content
            JSONObject json = new JSONObject(builder.toString());
            if (json.getJSONObject("responseData").getJSONArray("results").length() == 0) {
                return false;
            } else {
                int randNumb = (int) (Math.random() * 6);
                //chooses random picture of first 6 url returned
                String imageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(randNumb).getString("unescapedUrl"); //get the url-property of a json object              
                helper.add(imageUrl);   // add  founded picture to helper list

                if (UserInput == true) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (UnknownHostException e) {
            // if no internet connection                  
            System.out.println("no internet connection");
            return false;
        } catch (Exception e) {
            // catch all other exceptions
            System.out.println("something went wrong in the google search api");
            return false;
        }
    }
}
