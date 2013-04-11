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
     *       - comments
     *       -error if pic not found - invoke back up
     *       -üöäß replace
     */
    private final String START_URL="https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=1&imgsz=medium&as_filetype=jpg&userip=192.168.0.1&hl=de&q=";
    private  LinkedList<String> helperList= new <String>LinkedList();
    
    public void choosePicture(){
        
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
                  
        
       configureURLs();
       
       System.out.println(" nach dem configure ");
       
       int randomNumber = (int) (Math.random()*(PrGenerator.mainDatabase.getPictureList().size()));
       String choosenPicture = PrGenerator.mainDatabase.getPictureList().get(randomNumber);
       PrGenerator.mainDatabase.setChosenPicture(choosenPicture);
       
       for ( Iterator<String> i = PrGenerator.mainDatabase.getPictureList().iterator(); i.hasNext(); )
        {
        String s = i.next();
        System.out.println(s);
        
        }
       
       System.out.println("\n" +"Choosen Picture is " +PrGenerator.mainDatabase.getChosenPicture());
                
    }
    
    private void configureURLs(){
          System.out.println("START_URL " +START_URL);
         LinkedList <String> userInput=PrGenerator.mainDatabase.getUserInputFiltered();
         String[]  templateFill= PrGenerator.mainDatabase.getTemplateFill();
         String address;
         String helper;
               
        while (!userInput.isEmpty()) {
           helper=userInput.getFirst().replace(" ","+");
           System.out.println("helper " +helper);
           address = START_URL + helper;
           System.out.println("current address " +address);
           findPictures(address);
           userInput.removeFirst();           
        }    
        
        for (int i=0; i<templateFill.length; i++){
            address = START_URL + templateFill[i].replace(" ", "+");
            System.out.println("current address " +address);
            findPictures(address);            
        }
              
        PrGenerator.mainDatabase.setPictureList(helperList);        
        for ( Iterator<String> i = PrGenerator.mainDatabase.getPictureList().iterator(); i.hasNext(); )
        {
        String s = i.next();
        System.out.println(s);
        
        }
        System.out.println("last line in configure method");
    }
    
    private void findPictures(String address){
       
        try {
                URL url = new URL(address);
                URLConnection connection = url.openConnection();
                // URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyAoFTQH58kb6GEF-o0v7qKh5kMqPmZl5oo&cx=006298392676923811362:bsx_ivypxlm&q=dhbw&searchType=image");
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                JSONObject json = new JSONObject(builder.toString());                         
                String imageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(0).getString("url");
                System.out.println("alles im grünen Bereich =)");
                
                helperList.add(imageUrl);
                
                
            } catch (Exception e) { 
                
                e.printStackTrace();          

            }

    }
    
     
}
