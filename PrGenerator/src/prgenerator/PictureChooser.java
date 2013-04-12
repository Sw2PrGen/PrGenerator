/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import org.json.JSONObject;

/**
 *
 * @author Yuliya Kuznetsova
 */
public class PictureChooser {
    
    /*
     * TO DOs: 
     *       
     *       -error if pic not found or no connection - invoke back up        
     *       - find picts for completely input not only for separated words 
     */
        
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
    private final String PARAMETERS_URL="&rsz=1&imgsz=medium&as_filetype=jpg&userip=192.168.0.1&hl=de&q=";
    
    /*
     * Method for choosing a picture for the application,
     * invokes sendRequests() and then chooses randomly
     * a picture from a created pictures list
     * 
     */
    public void choosePicture() {
        
         //just for current testing
          
         LinkedList <String> myList=new <String>LinkedList();
         myList.add("alles banane");
         myList.add("zwei");
         myList.add("drei");
         String[] myTemplate = new String[3];
         myTemplate[0]="hot dog";
         myTemplate[1]="mannheim";
         myTemplate[2]="heidelberg";
         PrGenerator.mainDatabase.setTemplateFill(myTemplate);        
         PrGenerator.mainDatabase.setUserInputFiltered(myList);
         

        configureRequests(); 
        
        int randomNumber = (int) (Math.random() * (PrGenerator.mainDatabase.getPictureList().size()));
        String choosenPicture = PrGenerator.mainDatabase.getPictureList().get(randomNumber);
        PrGenerator.mainDatabase.setChosenPicture(choosenPicture);

        for (Iterator<String> i = PrGenerator.mainDatabase.getPictureList().iterator(); i.hasNext();) {
            String s = i.next();
            System.out.println(s);
        }
        
        System.out.println("\n" + "Choosen Picture is " + PrGenerator.mainDatabase.getChosenPicture());

    }
    
    /*
     * method which configures search parameter for google image search api,
     * key search words are: user input and main words from heading of a document
     * invokes findPictures (String adress) 
     * 
     */
    private void configureRequests() {

        LinkedList<String> userInput = PrGenerator.mainDatabase.getUserInputFiltered(); //filtered user input
        String[] templateFill = PrGenerator.mainDatabase.getTemplateFill(); // main words from heading
        String url;

        //fill in the url with the words user input 
        while (!userInput.isEmpty()) {
            url = START_URL + PARAMETERS_URL + userInput.getFirst().replace(" ", "+"); // replace blanks in  user input to get a proper url 
            //System.out.println("current address " + url);
            findPictures(url);
            userInput.removeFirst();
        }
        //fill in the url with the main words from heading
        for (int i = 0; i < templateFill.length; i++) {
            url = START_URL + PARAMETERS_URL + templateFill[i].replace(" ", "+");
            //System.out.println("current address " + url);
            findPictures(url);
        }
        
        PrGenerator.mainDatabase.setPictureList(helper); // set up the pictureList in the database with founded pictures
        
    }
    
    /*
     * method that searches for the pictures online with help of Google Image Search API
     * and adds founded pictures to the database in a pictureList.
     * For each request one image url will be  selected.
     * Uses JSON to get the Web-Content
     * 
     */
    private void findPictures(String address) {
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

            JSONObject json = new JSONObject(builder.toString());  // construct a JSONObject from page content
            String imageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(0).getString("url"); //get the url-property of a json object           
            helper.add(imageUrl);   // add  founded picture to helper list
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* other api 
         * URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyAoFTQH58kb6GEF-o0v7qKh5kMqPmZl5oo&cx=006298392676923811362:bsx_ivypxlm&q=dhbw&searchType=image");
         * String imageUrl = json.getJSONArray("items").getJSONObject(i).getString("link"); 
         */
    }
}
