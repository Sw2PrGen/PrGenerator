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
    private final String PARAMETERS_URL="&rsz=1&imgsz=medium&as_filetype=jpg&userip=192.168.0.1&hl=de&q=";
    
    /**
     * Method for choosing a picture for the application,
     * invokes sendRequests() and then chooses randomly
     * a picture from a created pictures list
     * 
     */
    public void choosePicture() {

        //just for current testing

//        LinkedList<String> myList = new <String>LinkedList();
//        myList.add("alles banane");
//        myList.add("zwei");
//        myList.add("drei");
//        String[] myTemplate = new String[3];
//        myTemplate[0] = "hot dog";
//        myTemplate[1] = "mannheim";
//        myTemplate[2] = "heidelberg";
//        PrGenerator.mainDatabase.setTemplateFill(myTemplate);
//        PrGenerator.mainDatabase.setUserInputFiltered(myList);
//        PrGenerator.mainDatabase.setCreatedHeading("dhbw mannheim ist die beste uni");

        configureRequests();

        if (PrGenerator.mainDatabase.getPictureList().isEmpty()) {
            //load back up to the database if no picture in the pictureList
            System.out.println("load backup");

        } else {
            //select random picture from a pictureList
            int randomNumber = (int) (Math.random() * (PrGenerator.mainDatabase.getPictureList().size()));
            String choosenPicture = PrGenerator.mainDatabase.getPictureList().get(randomNumber);
            PrGenerator.mainDatabase.setChosenPicture(choosenPicture);
            //show choosen picture just for current testing         
            try{
            BufferedImage image = ImageIO.read(new URL(choosenPicture));
            JOptionPane.showMessageDialog(null, "", "", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(image));
            }
            catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failure", JOptionPane.ERROR_MESSAGE);
            }
            
           /* System.out.println("pictureList: ");
            * for (Iterator<String> i = PrGenerator.mainDatabase.getPictureList().iterator(); i.hasNext();) {
                String s = i.next();
                System.out.println(s);
            }
            */
            System.out.println("\n" + "Choosen Picture is " + PrGenerator.mainDatabase.getChosenPicture());
        }
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
        String[] templateFill = PrGenerator.mainDatabase.getTemplateFill(); // main words from heading
        String url;            
        
        url=START_URL + PARAMETERS_URL + PrGenerator.mainDatabase.getCreatedHeading().replace(" ", "+"); //replace blanks in the heading to get a proper url 
        boolean foundedPicHeading=findPictures(url, true);   //true indicates, that it is a heading     
       
        if (!foundedPicHeading) {
            //if no picture for the whole heading found fill in the url with the words from the user input 
            while (!userInput.isEmpty()) {
                url = START_URL + PARAMETERS_URL + userInput.getFirst().replace(" ", "+"); // replace blanks in the user input to get a proper url 
                //System.out.println("current address " + url);
                findPictures(url, false); // false indicates, that it is not a heading   
                userInput.removeFirst();
            }
            //or fill in the url with the main words from heading
            for (int i = 0; i < templateFill.length; i++) {
                url = START_URL + PARAMETERS_URL + templateFill[i].replace(" ", "+"); //replace blanks within the main words from the heading to get a proper url 
                //System.out.println("current address " + url);
                findPictures(url, false);
            }
        }
        PrGenerator.mainDatabase.setPictureList(helper); // set up the pictureList in the database with founded pictures
        
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
    private boolean findPictures(String address, boolean heading) {
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
            
            reader.close(); //added by Dawid
            
            JSONObject json = new JSONObject(builder.toString());  // construct a JSONObject from page content
            String imageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(0).getString("url"); //get the url-property of a json object           
            helper.add(imageUrl);   // add  founded picture to helper list
            if (heading == true) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }

        /* other api 
         * URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyAoFTQH58kb6GEF-o0v7qKh5kMqPmZl5oo&cx=006298392676923811362:bsx_ivypxlm&q=dhbw&searchType=image");
         * String imageUrl = json.getJSONArray("items").getJSONObject(i).getString("link"); 
         */
    }
}
