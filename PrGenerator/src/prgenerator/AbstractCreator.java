/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 *
 * 
 * 
 * 
 * 
 * @author Dominik Künne
 * 
 * Abstractgenerator: Methode createAbstract() funktioniert. Als Eingabe wurde folgende XML-DAtei verwendet:
 * 
     <?xml version="1.0"?>
     <template>
	<templateOrt> Ich war in _location_ . </templateOrt>
	<templateOrt> Ich war nicht in _location_ .</templateOrt>
	<templateOrt> Ich wollte nach _location_ .</templateOrt>
	<templateOrt> Ich möchte gern nach _location_ .</templateOrt>
	<templateOrt> Ich werde nach _location_ gehen.</templateOrt>
	<templateDate> Es war der _date_ .</templateDate>
	<templateDate> Es war am _date_ .</templateDate>
	<templateDate> Es sollte der _date_ sein.</templateDate>
	<templateKeyAspect> Ich war hier um _keyAspect_ zu spielen.</templateKeyAspect>
	<templateKeyAspect> Ich war dort um _keyAspect_ zu spielen.</templateKeyAspect>
     </template>
 * 
 * 
 */


public class AbstractCreator {
    
    String whenSL[] = new String[1];
    String whenL[] = new String[19];
    String whereSL[] = new String[2];
    String notNounL[] = new String[5];
    
    LinkedList<String> whenWL = new LinkedList<>(); // when word list
    LinkedList<Integer> whenCL = new LinkedList<>(); // when couter list
    
    LinkedList<String> whereWL = new LinkedList<>(); 
    LinkedList<Integer> whereCL = new LinkedList<>(); 
    
    LinkedList<String> whatWL = new LinkedList<>(); 
    LinkedList<Integer> whatCL = new LinkedList<>(); 
    
    //String maintext = PrGenerator.mainDatabase.getCreatedText();
    String maintext = "Nach meiner Promotion habe ich Mittwoch heute eine Professur in Mannheim für ABWL, Personal und Organisation an der Ostfalia HAW in Wolfsburg verwaltet, bevor ich am 1.März 2013 an die DHBW Mannheim berufen wurde. Dies hat z.B. dazu geführt, dass ich an der Universität Mannheim gemeinsam mit Kollegen ein Seminar zur Mitarbeiterführung entwickelt habe, das stark auf meinen Erfahrungen aus der beruflichen Praxis aufbaute.Für Studieninteressierte, die gerne Studium und Praxisausbildung verbinden möchten, bietet das duale Studium an der DHBW eine attraktive Möglichkeit mit hervorragenden Berufsaussichten und Karrierechancen. ";


    public void initializeLists(){
        // kann man mehrere Werte gleichzeitig in Array schieben?
        
        // Montag|Dienstag|Mittwoch|Donnerstag|Freitag|Samstag|Sonnabend|Sonntag|heute|gestern|morgen|Januar|Februar|März|April|Mai|Juni|Juli|August|September|Oktober|November|Dezember|Weihnachten|Ostern|Silvester
        whenSL[0]= "am"; 
        
        whenL[0] ="Montag";
        whenL[1] ="Dienstag";
        whenL[2] ="Mittwoch";
        whenL[3] ="Montag";
        whenL[4] ="Montag";
        whenL[5] ="Montag";
        whenL[6]="Sonntag";
        whenL[7] = "Januar";
        whenL[18]="Dezember";
        
        whereSL[0] ="in";
        whereSL[1]="aus";
        
        notNounL[0]="Die";
        
    }
    
    
    public void analyzeText(){
        int ix = 0;
        maintext = maintext.replaceAll("\\. "," ");
        maintext = maintext.replaceAll("!","");
        maintext = maintext.replaceAll("\\?","");
        maintext = maintext.replaceAll(";","");
        maintext = maintext.replaceAll(",","");
        //maintext = maintext.replaceAll("\\s[^A-Za-z]{0,4}\\s","");
        
        
        LinkedList<String> words = new LinkedList<>(Arrays.asList(maintext.split(" ")));
        int txtLength = words.size();
//        System.out.println(txtLength);
//        System.out.println(words.get(0));
//        System.out.println(words.get(txtLength-1));
        
        
        for (int i =0; i<txtLength; i++){
            String nWord = words.get(i);
            
            if (nWord.matches(".*(Montag|Dienstag|Mittwoch|Donnerstag|Freitag|Samstag|Sonnabend|Sonntag|heute|gestern|morgen|Weihnachten|Ostern|Silvester).*"))
            { // store directly time word
                // storeWord(nWord, whenWL, whenCL);
                System.out.println("1 "+nWord);
            } else if (nWord.matches(".*(Januar|Februar|März|April|Mai|Juni|Juli|August|September|Oktober|November|Dezember).*"))
            { // store special date --> proof!
                //storeWordwD(nWord, i, whenWL, whenCL); // store word with date word,stelle in Liste, WL, CL
                System.out.println("2 "+nWord);
            } else if (nWord.matches(".*(Woche|Wochen).*")){
                                              
            } else if (nWord.matches("[A-Z]{1,}.*"))
            { 
                //storeWord(nWord, whatWL, whatCL);
                System.out.println("4 "+nWord);
            } else if (nWord.matches("in|aus"))
            {
                if (words.get(i+1).matches("[A-Z]{1,}.*")){
                    //storeWord(words.get(i+1),whereWL, whereCL);
                    System.out.println("5 "+nWord +" "+ words.get(i+1));
                }
            }
            
        }
        //String nWord = nextWord(0);
        System.out.println(maintext);
    }
    
  
    public void createAbstract(String path, String location, String date, String keyAspect){
        
        Template templateReader = new Template();
        String abstractTemplateQuery = "";
        if(location != null){
            abstractTemplateQuery += templateReader.readXML(path, "templateOrt");
            abstractTemplateQuery = abstractTemplateQuery.replace("_location_", location);
        }
        
        if(date != null){
            abstractTemplateQuery += templateReader.readXML(path, "templateDate");
            abstractTemplateQuery = abstractTemplateQuery.replace("_date_", date);
        }
        if (keyAspect != null){
            abstractTemplateQuery += templateReader.readXML(path, "templateKeyAspect");
            abstractTemplateQuery = abstractTemplateQuery.replace("_keyAspect_", keyAspect);
        }
        
        

        System.out.println(abstractTemplateQuery);
       
    }
   public static void main(String [] arg){
       AbstractCreator abstractCreator = new AbstractCreator();
       //abstractCreator.createAbstract("d:\\xmlTemplate.xml", "Bad Kreuznach", "12.12.12", "Fussball");
       abstractCreator.initializeLists();
       abstractCreator.analyzeText();
   } 
}



    
    
