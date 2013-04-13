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
    String maintext = "Nach meiner August Montag heute Montag 1. Februar 2012 12.März Dezember 2012 Promotion habe ich Mittwoch heute eine Professur 3 Wochen einer Woche eine Woche fünf Wochen in Mannheim für ABWL, Personal und Organisation an der Ostfalia HAW in Wolfsburg verwaltet, bevor ich am 1.März 2013 an die DHBW Mannheim berufen wurde. Dies hat z.B. dazu geführt, dass ich an der Universität Mannheim gemeinsam mit Kollegen ein Seminar zur Mitarbeiterführung entwickelt habe, das stark auf meinen Erfahrungen aus der beruflichen Praxis aufbaute.Für Studieninteressierte, die gerne Studium und Praxisausbildung verbinden möchten, bietet das duale Studium an der DHBW eine attraktive Möglichkeit mit hervorragenden Berufsaussichten und Karrierechancen. August. ";


//    public void initializeLists(){
//        // kann man mehrere Werte gleichzeitig in Array schieben?
//        
//        // Montag|Dienstag|Mittwoch|Donnerstag|Freitag|Samstag|Sonnabend|Sonntag|heute|gestern|morgen|Januar|Februar|März|April|Mai|Juni|Juli|August|September|Oktober|November|Dezember|Weihnachten|Ostern|Silvester
//        whenSL[0]= "am"; 
//        
//        whenL[0] ="Montag";
//        whenL[1] ="Dienstag";
//        whenL[2] ="Mittwoch";
//        whenL[3] ="Montag";
//        whenL[4] ="Montag";
//        whenL[5] ="Montag";
//        whenL[6]="Sonntag";
//        whenL[7] = "Januar";
//        whenL[18]="Dezember";
//        
//        whereSL[0] ="in";
//        whereSL[1]="aus";
//        
//        notNounL[0]="Die";
//        
//    }
    
    public void storeWord(String sWord, List WL, List CL){
        if (WL.contains(sWord)){
            int i = WL.indexOf(sWord);          // als Wahrheitswert benutzen?
                //System.out.println(WL.get(i)+" "+CL.get(i));
            int counter = (Integer) CL.get(i);  // current value of counter
            counter++;  // inc counter
            CL.remove(i);       // delete list object at index i
            CL.add(i, counter); // add list object at index i
                                // old list object has to be removed because with "add" it would not be overwriten 
                //System.out.println(WL.get(i)+" "+CL.get(i));
        } else {
            // adds new word and sets counter to 1
            WL.add(sWord); 
            CL.add(1);
        }
    }
    
    public void analyzeText(){
        
        maintext = maintext.replaceAll("([0-9]{1,2})\\. ","$1."); // alle "ZAHL. " in "ZAHL." umwandeln
        maintext = maintext.replaceAll("\\. "," ");     // alle ". " in " " umwandeln
        maintext = maintext.replaceAll("([0-9]{1,2})\\.","$1. ");   // alle "ZAHL." in "ZAHL. " umwandeln
        maintext = maintext.replaceAll("!","");
        maintext = maintext.replaceAll("\\?","");
        maintext = maintext.replaceAll(";","");
        maintext = maintext.replaceAll(",","");
        
        
        LinkedList<String> words = new LinkedList<>(Arrays.asList(maintext.split(" ")));
        int txtLength = words.size();
       
        for (int i =0; i<txtLength; i++){
            String nWord = words.get(i);
            
            //store "when" words
            if (nWord.matches(".*(Montag|Dienstag|Mittwoch|Donnerstag|Freitag|Samstag|Sonnabend|Sonntag|heute|gestern|morgen|Weihnachten|Ostern|Silvester)"))
            { 
                storeWord(nWord, whenWL, whenCL);
                System.out.println("1 "+nWord);
                
            } else if (nWord.matches(".*(Januar|Februar|März|April|Mai|Juni|Juli|August|September|Oktober|November|Dezember).*")){
                
                // add number of day within the month 
                if (i-1 >=0){ // if month is at the beginning of the text
                    if (words.get(i-1).matches("[0-9]{1,2}\\.")){
                        nWord = words.get(i-1)+" "+nWord;
                    }
                }
                
                // add year
                if (i+1 <txtLength){ // if end of text is reached with the month
                    if (words.get(i+1).matches("[0-9]{1,4}")){
                        nWord = nWord +" "+words.get(i+1);
                    }
                }
                // -->  DO TO: Es kann einmal 1. Januar 2012 genau stehen und dann aber weiter mit "im Januar" gehen.
                //      Dann vielleicht nochmal zusätzlich einfach den Monat alleine Zählen
                
                storeWord(nWord, whenWL, whenCL); // store word with date word, WL, CL
                System.out.println("2 "+nWord);
                
            } else if (nWord.matches(".*(Woche|Wochen).*")){
                if (i-1 >=0){
                    if (words.get(i-1).matches("[0-9]{1,2}|einer|zwei|drei|vier|fünf|sechs|sieben|acht|neun|zehn")){
                        nWord = words.get(i-1) +" "+nWord;
                        storeWord(nWord,whenWL, whenCL);
                        System.out.println("3 "+nWord);
                    }
                }
            
            // "what" words: stores words which stat with a capital letter
            } else if (nWord.matches("[A-Z]{1,}.*")){ 
                //storeWord(nWord, whatWL, whatCL);
                System.out.println("4 "+nWord);
                
            // stores locations    
            } else if (nWord.matches("in|aus")){
                if (i+1 < txtLength){
                    if (words.get(i+1).matches("[A-Z]{1,}.*")){
                        nWord = words.get(i+1);       
                        //nWord = nWord +" "+ words.get(i+1); // if with additional words like "in, aus"
                        storeWord(nWord,whereWL, whereCL);
                    }
                }

                System.out.println("5 "+nWord);
            }
            
        }
        System.out.println(maintext);
        System.out.println(whenWL +" "+whenCL);
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
       abstractCreator.analyzeText();
   } 
}



    
    
