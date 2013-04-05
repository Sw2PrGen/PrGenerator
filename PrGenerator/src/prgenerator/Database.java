/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.util.LinkedList;

/**
 *
 * @author rusinda
 */
public class Database {
    
    private LinkedList<String> currentData;
    private String userInput;
    private String createdText;
    private String createdAbstract;
    private String createdHeading;
    private LinkedList<String> templatesAbstract; //<Template>
    private LinkedList<String> templatesHeading;  //<Template>
    private String finalDocument;
    private String finalHtmlDokument;
    private LinkedList<String> pictureList;
    private LinkedList<String> userInputFiltered;
    private String[] templateFill;
    private String chosenPicture;
    
    private boolean loadNewData(){
        return true;
    }
    
    private boolean loadBackup(){
        return true;
    }
    
    private boolean updateBackup(){
        return true;
    }
    
    private boolean isLoaded() {
        return true;
    }
    
    public void manageData(){
        
    }
    
}
