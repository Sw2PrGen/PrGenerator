/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * @author Dominik Künne, Jörg Woditschka, Tobias Mauritz
 * Abstractgenerator: Methode createAbstract() funktioniert. Als Eingabe wurde folgende XML-DAtei verwendet:
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
    
    
    String maintext;// = PrGenerator.mainDatabase.getCreatedText();
    //String maintext = "Nach meiner In August Montag heute Montag 1. Februar 2012 12.März Dezember 2012 Promotion habe ich Mittwoch heute eine Professur 3 Wochen einer Woche eine Woche fünf Wochen in Mannheim für ABWL, Personal und Organisation an der Ostfalia HAW in Wolfsburg verwaltet, bevor ich am 1.März 2013 an die DHBW Mannheim berufen wurde. Dies hat z.B. dazu geführt, dass ich an der Universität Mannheim gemeinsam mit Kollegen ein Seminar zur Mitarbeiterführung entwickelt habe, das stark auf meinen Erfahrungen aus der beruflichen Praxis aufbaute.Für Studieninteressierte, die gerne Studium und Praxisausbildung verbinden möchten, bietet das duale Studium an der DHBW eine attraktive Möglichkeit mit hervorragenden Berufsaussichten und Karrierechancen. August. ";

    
    
    /**
     * 
     * @author Tobias Mauritz
     * @param sWord
     * @param WL
     * @param CL 
     */
    public void storeWord(String sWord, List WL, List CL){
        if (WL.contains(sWord)){
            int i = WL.indexOf(sWord);          // als Wahrheitswert benutzen?
                //System.out.println(WL.get(i)+" "+CL.get(i));
            int counter = (Integer) CL.get(i);  // current value of counter
            counter++;  // inc counter
            CL.remove(i);       // delete list object at index i
            CL.add(i, counter); // add list object at index i with new counter value
                                // old list object has to be removed because with "add" it would not be overwriten 
                //System.out.println(WL.get(i)+" "+CL.get(i));
        } else {
            // adds new word and sets counter to 1
            WL.add(sWord); 
            CL.add(1);
        }
    }
    
    
    /**
     * 
     * @author Tobias Mauritz
     * @param CL
     * @return 
     */
    public int getWordIndex(List CL){
        int min = 0;
        int idx = 0;
        
        // looks from last element of the list if the counter_new >= counter_old
        // so the word which was first found and is mostly used will be applied
        for (int i= CL.size()-1;i>=0;i--){
            if ((Integer)CL.get(i)>=min){
                min = (Integer)CL.get(i);
                idx =i;
            }
        }
        return idx;
    }
    
    /**
     * 
     * @author Tobias Mauritz
     */
    public void analyzeText(){    // when live!!!!!!!
    //public String[] analyzeText(){
        maintext = PrGenerator.mainDatabase.getCreatedText();
        
        
        maintext = maintext.replaceAll("([0-9]{1,2})\\. ","$1.");   // alle "ZAHL. " in "ZAHL." umwandeln
        maintext = maintext.replaceAll("\\. "," ");                 // alle ". " in " " umwandeln
        maintext = maintext.replaceAll("([0-9]{1,2})\\.","$1. ");   // alle "ZAHL." in "ZAHL. " umwandeln
        maintext = maintext.replaceAll("!","");
        maintext = maintext.replaceAll("\\?","");
        maintext = maintext.replaceAll(";","");
        maintext = maintext.replaceAll(",","");
        
        
        LinkedList<String> words = new LinkedList<>(Arrays.asList(maintext.split(" ")));
        int txtLength = words.size();
        
        for (int i =0; i<txtLength; i++){
            String nWord = words.get(i);
            
            //modified by Jörg: store dates needing an "am" 
            if (nWord.matches("(Montag|Dienstag|Mittwoch|Donnerstag|Freitag|Samstag|Sonnabend|Sonntag)"))
            { 
                storeWord("am "+nWord, whenWL, whenCL);
               
                System.out.println("1 "+nWord);
                
            //modified by Jörg: store dates which can be inserted without an "am"
            } else if (nWord.matches("(heute|gestern|morgen|Weihnachten|Ostern|Silvester)"))
            { 
                storeWord(nWord, whenWL, whenCL);
               
                System.out.println("1 "+nWord);
                
            } else if (nWord.matches("(Januar|Februar|März|April|Mai|Juni|Juli|August|September|Oktober|November|Dezember)")){
                
                // add number of day within the month 
                if (i-1 >=0){ // if month is at the beginning of the text
                    if (words.get(i-1).matches("[0-9]{1,2}\\.")){
                        nWord = "am "+words.get(i-1)+" "+nWord;
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
                
            } else if (nWord.matches("(Woche|Wochen)")){
                if (i-1 >=0){
                    if (words.get(i-1).matches("[0-9]{1,2}|einer|zwei|drei|vier|fünf|sechs|sieben|acht|neun|zehn")){
                        nWord = words.get(i-1) +" "+nWord;
                        storeWord(nWord,whenWL, whenCL);
                        System.out.println("3 "+nWord);
                    }
                }
             
            // stores locations    
            } else if (nWord.matches("in|aus")){
                if (i+1 < txtLength){
                    if (words.get(i+1).matches("[A-Z]{1,}[a-z]{1,}")&& !nWord.matches("Versehen|Bearbeitung|Betrieb")){
                        nWord = words.get(i+1);       
                        //nWord = nWord +" "+ words.get(i+1); // if with additional words like "in, aus"
                        storeWord(nWord,whereWL, whereCL);
                        i++;    // if there was a Noun after "in,aus" (maybe a location) 
                                // this will not saved into the list with words with capital letter
                    }
                }

                System.out.println("4 "+nWord);
                
            // "what" words: stores words which stat with a capital letter
            // TO DO: Alle Wörter mit Großbuchstaben am Anfang + mindestens 3 Zeichen (Der, Die, Das fällt weg, aber Nach oder Außerdem nicht
                // Liste erweitern!
            } else if (nWord.matches("[A-Z]{1,}.{3,}") && !nWord.matches("Nach|Außerdem|Dies|DHBW|Prof|Mannheim|Baden-Württemberg")){ 
                storeWord(nWord, whatWL, whatCL);
                System.out.println("5 "+nWord);
            }
            
        }
        
        int whereidx = getWordIndex(whereCL);
        int whenidx = getWordIndex(whenCL);
        int whatidx = getWordIndex(whatCL);
        
        System.out.println(whenidx);
        System.out.println(whenWL.get(whenidx));
        String[] awords = {whereWL.get(whereidx), whenWL.get(whenidx), whatWL.get(whatidx)};
        System.out.println(Arrays.toString(awords));
        
        // saves the three words Where, When, What into Database
        PrGenerator.mainDatabase.setTemplateFill(awords);     // when live!!!!!
        
        System.out.println(maintext);
        System.out.println(whenWL +" "+whenCL);
        System.out.println(whereWL +" "+whereCL);
        System.out.println(whatWL +" "+whatCL);
        
        //return awords;
    }
    
       /**
        * 
        */
       public void createAbstract(){
       analyzeText();

       //String path = "src\\sources\\templates_abstract.xml";
       String path = "/sources/templates_abstract.xml";
       String location = PrGenerator.mainDatabase.getTemplateFill()[0];
       String date = PrGenerator.mainDatabase.getTemplateFill()[1];
       String keyAspect = PrGenerator.mainDatabase.getTemplateFill()[2];
       
       
        
      /*
       * in each array element of the array sentences there is one type of template loaded
       * position 0: templateLocation
       * position 1: templateDate
       * position 2: templateKeyAspect
       * position 3: templateKeyAspectandLocation
       * position 4: templateLocationandDate
       */
     
      
       //added by Jörg: dynamic approach for concatenating sentences 
       String abstractTemplateQuery=""; //String to be returned as abstract
       Template template = new Template();
       Random generator = new Random(); // a random number generator helps picking sentences for the abstract   
       
       String nextType="DHBW"; //
       int nonCounter = 0;
       LinkedList<String> availableTagNames = new LinkedList(); //list of available TagNames. Get's reduced during the picking.
       availableTagNames.add("templateLocation");
       availableTagNames.add("templateDate");
       availableTagNames.add("templateKeyAspect");
       
       //this loop adds templates to the abstractTemplateQuery to create an abstract
       for(int i=0; i<3; i++){
           
           //picking a TagName
           int pickedTagName=generator.nextInt(availableTagNames.size());
           String tagName=availableTagNames.get(pickedTagName);
           availableTagNames.remove(pickedTagName);
           
           //add another tag to the TagName 
           if(availableTagNames.size()>1 && generator.nextBoolean()){
               int pickedTagName2=generator.nextInt(availableTagNames.size());
               String tagName2=availableTagNames.get(pickedTagName2); 
               availableTagNames.remove(pickedTagName2);
               tagName=(pickedTagName>pickedTagName2)?tagName2+tagName.substring(8):tagName+tagName2.substring(8); 
               i++;
           }
           
           //pick a Type and generate abstractTemplateQuery
           if(generator.nextInt(2)==0 && nonCounter<2){
               abstractTemplateQuery+=template.readXML(path, tagName, "None");
               nonCounter++;
               nextType="DHBW";
           }else{
               abstractTemplateQuery+=template.readXML(path, tagName, nextType);
               nextType=(nextType.equals("DHBW"))?"Sie":"DHBW";
           }
       }
       
       //"am" -> "Am" & "heute" -> "Heute"
       if(abstractTemplateQuery.indexOf("_date_")==1 ||(abstractTemplateQuery.indexOf("_date_")>1 && abstractTemplateQuery.charAt(abstractTemplateQuery.indexOf("_date_")-2)=='.')){
           date=date.substring(0,1).toUpperCase()+date.substring(1);
       }
       
       //replace place holders in abstractTemplateQuery
       abstractTemplateQuery=abstractTemplateQuery.replace("_location_", location).replace("_date_", date).replace("_keyAspect_", keyAspect);
       
       
       /*
       String[] sentences= new String[5]; 
        
        Template templateReader = new Template();
        String abstractTemplateQuery = "";
        //initialising array
        for ( int i = 0; i < sentences.length; i++ ) {
           sentences[i] = "";
       }
        if(location != null){
            sentences[0] = templateReader.readXML(path, "templateLocation")[0];
            sentences[0] = sentences[0].replace("_location_", location);
        }
        
        if(date != null){
            sentences[1] = templateReader.readXML(path, "templateDate")[0];
            sentences[1] = sentences[1].replace("_date_", date);
        }
        if (keyAspect != null){
            sentences[2] = templateReader.readXML(path, "templateKeyAspect")[0];
            sentences[2] = sentences[2].replace("_keyAspect_", keyAspect);
        }
        
        if ((keyAspect != null) && (location != null)){
            sentences[3] = templateReader.readXML(path, "templateKeyAspectandLocation")[0];
            sentences[3] = sentences[3].replace("_keyAspect_", keyAspect);
            sentences[3] = sentences[3].replace("_location_", location);
        }
        
        if ((date != null) && (location != null)){
            sentences[4] = templateReader.readXML(path, "templateLocationandDate")[0];
            sentences[4] = sentences[4].replace("_date_", date);
            sentences[4] = sentences[4].replace("_location_", location);
        }
        
        
        
        
        //randomly a predefined combination how the template sentences are concatenated is chosen
        Random generator = new Random();
        int combinations = 11;
        int i = combinations - generator.nextInt(combinations);
        
        switch(i){
            case 1: abstractTemplateQuery = sentences [0] + sentences [1] + sentences [2];
                    break;
            case 2: abstractTemplateQuery = sentences [0] + sentences [2] + sentences [1];
                    break;
            case 3: abstractTemplateQuery = sentences [1] + sentences [0] + sentences [2];
                    break;
            case 4: abstractTemplateQuery = sentences [1] + sentences [2] + sentences [0];
                    break;
            case 5: abstractTemplateQuery = sentences [2] + sentences [0] + sentences [1];
                    break;
            case 6: abstractTemplateQuery = sentences [2] + sentences [1] + sentences [0];
                    break;
            case 7: abstractTemplateQuery = sentences [2] + sentences [1] + sentences [0];
                    break;
            case 8: abstractTemplateQuery = sentences [1] + sentences [3];
                    break;
            case 9: abstractTemplateQuery = sentences [3] + sentences [1];
                    break;
            case 10: abstractTemplateQuery = sentences [2] + sentences [4];
                    break;
            case 11 : abstractTemplateQuery = sentences [4] + sentences [2];
                    break;
        
        }*/
        PrGenerator.mainDatabase.setCreatedAbstract(abstractTemplateQuery);
        System.out.println(abstractTemplateQuery);
   }
  //  private void fetchSentences(int i,String path, String tagName){
    //    Template templateReader = new Template();
      //  sentences[i] = templateReader.readXML(path, tagName);
         public static void main(String [ ] args)
{
      AbstractCreator abstractCreator = new AbstractCreator();
      abstractCreator.createAbstract();
            // Deliver the robot to the origin (1,1),
            // facing East, with no beepers.
     
}   
        }   

//   public static void main(String [] arg){
    //   AbstractCreator abstractCreator = new AbstractCreator();
       
       //abstractCreator.analyzeText();     // when live!!!!!
       //String[] templateFill = abstractCreator.analyzeText();

       //String[] templateFill = PrGenerator.mainDatabase.getTemplateFill();    // when live!!!!!!!
       
       //abstractCreator.createAbstract("src/sources/templates_abstract.xml", (String)templateFill[0], (String)templateFill[1], (String)templateFill[2]);
  //     abstractCreator.createAbstract();
//}



//}



    
    
