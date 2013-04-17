/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Stefan Dreher, Yuliya Kuznetsova
 */
public class TextCreator {
             
    
    private static LinkedList databaseText =new <String>LinkedList(); // PrGenerator.mainDatabase.get...copy of the date from database
    private  LinkedList preText = new <String>LinkedList();     // list for founded sentences that match with user input
    private  LinkedList dhText = new <String>LinkedList();      // list for founded sentences that have a relation to DHBW
    private  LinkedList placeText =new <String>LinkedList();    // list for founded sentences that contain a location/place 
    private  LinkedList timeText =new <String>LinkedList();     // list for founded sentences that contain time
    private  LinkedList finalText=new <String>LinkedList();     // finalText
         
/**
 * Method to find a sentence with a relation to a special time
 * 
 */
    private void findTime() {
        
        String current;      // temporary String for the current list element
        int count = 0;      //  counter for number of founded sentences    
        Collections.shuffle(databaseText);
        //itearation trough the whole database
        for (Iterator<String> i = databaseText.iterator(); i.hasNext();) {
            current =  i.next();
            //checking if sentence says something about a relation to a special time
            if (current.matches(".*\\s(Montag|Dienstag|Mittwoch|Donnerstag|Freitag|Samstag|Sonnabend|Sonntag|heute|gestern|morgen|Januar|Februar|März|April|Mai|Juni|Juli|August|September|Oktober|November|Dezember|Weihnachten|Ostern|Silvester)\\s.*")) {
                timeText.add(current);
                count = count + 1;
                databaseText.remove(i); // deletion of found sentence to avoid doubling
                if (count > 2) {  
                    break; // condition to break the loop if more than 2 sentences are found 
                }                
            }
        }
        System.out.println("timeText: ");
         for (Iterator<String> i = timeText.iterator(); i.hasNext();) {
                String s = i.next();
                System.out.println(s);
            }
    }

    /**
     * 
     * method to find a description of a place
     */      
      private void findPlace() {
          
        String current;  //string for the current list element  
        int count = 0; //counter for number of founded sentences        

        Collections.shuffle(databaseText);
        Iterator<String> i = databaseText.iterator();
        //iteration through the list to find a place
        while (i.hasNext()) {
            
     
            current = i.next();
            //check with regular expression if there are "in" in the sentence and after that a capital letter in order to indicate a place in a sentence
            if (current.matches(".*\\sin\\s[A-Z].*")) {
                placeText.add(current);
                count = count + 1;
                i.remove(); //deletion of found sentence to avoid doubling
                if (count > 2) {
                    break; // condition to break the loop if more than 2 sentences are found 
                }                
            }
        }
        System.out.println("placeText: " );
         for (Iterator<String> it = placeText.iterator(); it.hasNext();) {
                String s = it.next();
                System.out.println(s);
            }
    }

    /**
     *
     * 
     * Funtion to find a sentence related to the DH
     */
    private void findDhRelation() {
        
        String current; //String for the current sentence
        int counter = 0;  //counter for number of founded sentences
Collections.shuffle(databaseText);
        // Iteration through the list to find a sentece about the DH

           Iterator<String> i = databaseText.iterator();
           


       while( i.hasNext()) {
            current = i.next();
            if (current.contains("DHBW")) {
                dhText.add(current);
                i.remove(); //deletion of found sentence to avoid doubling
                counter++;
                if (counter > 2) {
                    break; // condition to break the loop if more than 2 sentences are found
                }
            }
           else if (current.contains("DH")) {
                dhText.add(current);
                i.remove(); //deletion of found sentence to avoid doubling
                counter++;
                if (counter > 2) {
                    break; // condition to break the loop if more than 2 sentences are found
                }
            }
           else if (current.contains("Duale Hochschule Baden")) {
                dhText.add(current);
                i.remove(); //deletion of found sentence to avoid doubling
                counter++;
                if (counter > 2) {
                    break; // condition to break the loop if more than 2 sentences are found
                }
            }
        }
       System.out.println("dhText: " ); 
       for (Iterator<String> it = dhText.iterator(); it.hasNext();) {
                String s = it.next();
                System.out.println(s);
            }
    }
    
    /**
     * Method to find sentences related to the user input
     * @param input filtered user input
     */
    private void findInput(LinkedList input) {
System.out.println( "\n" + "davor: UserInputFiltered is Empty: " +PrGenerator.mainDatabase.getUserInputFiltered().isEmpty());
        String current; //current sentence        
        String currentinput; //current user input
       Collections.shuffle(databaseText);
        
        //iteration through the list to find all sentences with a relation to the user input
        while (!input.isEmpty()) {
            
            System.out.println("wir sind im inputsucher daskhka hhaksfk gk a ksg fkga skfgaskghf asgkgdgfgajhdsgfjd");
            currentinput = (String) input.getFirst().toString().toLowerCase(); 
           // System.out.println(currentinput);
            
            Iterator<String> i = databaseText.iterator();
           while( i.hasNext()) {
                current = i.next();
                if (current.toString().toLowerCase().contains(currentinput)) {  // check for matches of user input with current sentence from the database (both uncapitalized)
                    preText.add(current);
                    i.remove(); //deletion of found sentence to avoid doubling
                }
            }
            input.removeFirst(); 
        }
         System.out.println("preText: " );
          for (Iterator<String> it = preText.iterator(); it.hasNext();) {
                String s = it.next();
                System.out.println(s);
            }
          System.out.println( "\n" + "danach: UserInputFiltered is Empty: " +PrGenerator.mainDatabase.getUserInputFiltered().isEmpty());
    }
    
    /** 
     * Method to combine all the found sentences to a complete text 
     * 
     */
    private void selectSentences() {

        //adding all the textparts to the final text
        finalText.addAll(dhText);
        finalText.addAll(timeText);
        finalText.addAll(placeText);

        //adding the sentence from the user input;  
        for (int i = 1; i < 21 - finalText.size(); i++) {
            if (i >= preText.size()) {
                break;
            }
            finalText.add(preText.get(i));
        }
        int randomsize=(int) (Math.random()*10);
        int randomNumber;
        System.out.println("databaseText " +databaseText.size());
        //optional filling of the text with random sentences if there where not found enough sentences related to the user input
        for (int i = 0; i < 10+randomsize - finalText.size(); i++) {
            randomNumber = (int) (Math.random() * (databaseText.size() + 1));
            System.out.println("random " +randomNumber);
            finalText.add(databaseText.get(randomNumber));
        }
        //System.out.println(finaltext);
    }
    /**
     * 
     * Method for creating the main text of a programm.
     * Invokes findTime(), findPlace(), findDhRelation(), findInput() and selectSentences(),
     * then shuffles the final text to get random order of sentences 
     * 
     */
    public void createMainText() {
        finalText.clear();
        preText.clear();
        dhText.clear();
        timeText.clear();
        placeText.clear();
        LinkedList liste = new <String>LinkedList();
        LinkedList text = new <String>LinkedList();
        LinkedList input = new <String>LinkedList();

        liste.add("HALLO Die Duale Hochschule Baden-Würtemberg hat noch freie Plätzefür 2014");
        liste.add("Am Montag den 23 April findet eine Inforverantsaltung zu künstlicher Intelligenz statt");
        liste.add("Viele Studenten werden für die Vorstellung der neuen Elefanten erwartet");
        liste.add("Das Wohltätigleitskponzert der Hochschule war ein voller Erfolg für alle anwesenden");
        liste.add("Viele Studierende der MINT-Fächer (Mathematik, Informatik, Naturwissenschaften, Technik) scheitern bereits in den ersten Semestern an den Herausforderungen in der Mathematik");
        liste.add("Während andere Hochschulen erst kurz vor Studienbeginn ihr Studierendenportfolio kennen, und damit nur allgemeine Angebote für Vorkurse anbieten können, haben die Dualen Partner den Vorteil, sich schon Monate im Voraus einen Überblick über ihre Studienanfänger verschaffen zu können, da sich die Studierenden der DHBW schon ein bis eineinhalb Jahre vor Studienbeginn bei den Partnerunternehmen bewerben");
        liste.add("Professor Dr.-Ing. Bernd Mahn unterstützt seit dem 1. Juli den Studiengang Wirtschaftsingenieurwesen an der DHBW Mannheim");
        liste.add("Das Wirtschaftsingenieurwesen liegt an der Schnittstelle von Technik und Betriebswirtschaft.");
        liste.add("Der wissenschaftliche Leiter Prof. Dr. Lothar Weinland zeigt sich von den neuen Möglichkeiten begeistert: „Das ZEEB bildet einen Meilenstein zur Integration von empirischen und experimentellen Forschungsansätzen in die duale Hochschulausbildung");
        liste.add("Am Tag der Eröffnung blicken die Verantwortlichen bereits in die Zukunft. Obwohl das Konzept schon jetzt an seine räumlichen Grenzen stößt, sind weitere Ausbaustufen bereits in Planung.");
        liste.add("Genau diesen interdisziplinären Fragestellungen werden die Studierenden in interkulturell gemischten Workshop-Gruppen am Beispiel der Olympischen Spiele in London, der Fußball- Europameisterschaft 2012 und der Frankfurter Buchmesse in den nächsten Tagen nachgehen.");
        liste.add("Für die Studierenden des 5. Semesters bietet das ISEM unter Leitung von Prof. Dr. Ursina Böhm, Dipl.-Angl. Sabine Matejek, Prof. Dr. Martin Kornmeier und Prof. Dr. Bernhard Ling nicht nur die Möglichkeit, sich mit dem Seminarthema intensiv und fachübergreifend auseinander zu setzen, sondern auch die Gelegenheit interkulturelles Wissen anzuwenden, Teamwork-, Präsentations- und Recherchefähigkeiten und -techniken zu verbessern sowie internationale Freundschaften zu schließen.");
        liste.add("Am Freitag, 23. November 2012, feierte die Duale Hochschule Baden-Württemberg (DHBW) Mannheim im Mannheimer Rosengarten ab 19.00 Uhr bei der legendären Night of the Graduates in einer rauschenden Ball- und Partynacht mit weit über 6000 Gästen die Ehrung eines der stärksten Absolventenjahrgänge ihrer Geschichte.");
        liste.add("Die DHBW Mannheim ließ es sich nicht nehmen, diesen Erfolg in einer gigantischen Ballnacht zu feiern und die rund 1.700 Absolventen, deren Freunden, Familien, Firmenvertretern, Professoren und Hochschulangehörigen im Mannheimer Rosengarten standesgemäß mit hochkarätigen Liveacts, einem bunten Showprogramm und der Ehrung der Besten zu verabschieden.");
        liste.add("Albrecht Hornbach, Vorstand der Hornbach Holding AG, der bereits zuvor eine Rede vor der Company Lounge, der Ehrenlounge für Firmenvertreter, hielt, in der er über die Herausforderungen eines global agierenden Unternehmens referierte");
        liste.add("Schon zu Beginn des Hoodie-Verkaufs hat sich die Studierendenvertretung darauf festgelegt, einen Teil des Erlöses an das Kinderhospiz Sterntaler zu spenden");
        liste.add("Dass gerade auch vor diesem schwierigen Thema die Studierenden ihre Auge nicht verschließen, liegt Alexandra Rieker besonders am Herzen");
        liste.add("Von 1999 bis 2004 habe ich in Magdeburg Informatik studiert und war dann ein Jahr am Fraunhofer Institut für Autonome Informationssysteme in Sankt Augustin.");
        liste.add("Die Duale Hochschule Baden-Würtemberg hat noch freie Plätzefür 2014");
        liste.add("Am Montag den 23 April findet eine Inforverantsaltung zu künstlicher Intelligenz statt");
        liste.add("Viele Studenten werden für die Vorstellung der neuen Elefanten erwartet");
        liste.add("Das Wohltätigleitskponzert der Hochschule war ein voller Erfolg für alle anwesenden");
        liste.add("Viele Studierende der MINT-Fächer (Mathematik, Informatik, Naturwissenschaften, Technik) scheitern bereits in den ersten Semestern an den Herausforderungen in der Mathematik");
        liste.add("Während andere Hochschulen erst kurz vor Studienbeginn ihr Studierendenportfolio kennen, und damit nur allgemeine Angebote für Vorkurse anbieten können, haben die Dualen Partner den Vorteil, sich schon Monate im Voraus einen Überblick über ihre Studienanfänger verschaffen zu können, da sich die Studierenden der DHBW schon ein bis eineinhalb Jahre vor Studienbeginn bei den Partnerunternehmen bewerben");
        liste.add("Professor Dr.-Ing. Bernd Mahn unterstützt seit dem 1. Juli den Studiengang Wirtschaftsingenieurwesen an der DHBW Mannheim");
        liste.add("Das Wirtschaftsingenieurwesen liegt an der Schnittstelle von Technik und Betriebswirtschaft.");
        liste.add("Der wissenschaftliche Leiter Prof. Dr. Lothar Weinland zeigt sich von den neuen Möglichkeiten begeistert: „Das ZEEB bildet einen Meilenstein zur Integration von empirischen und experimentellen Forschungsansätzen in die duale Hochschulausbildung");
        liste.add("Am Tag der Eröffnung blicken die Verantwortlichen bereits in die Zukunft. Obwohl das Konzept schon jetzt an seine räumlichen Grenzen stößt, sind weitere Ausbaustufen bereits in Planung.");
        liste.add("Genau diesen interdisziplinären Fragestellungen werden die Studierenden in interkulturell gemischten Workshop-Gruppen am Beispiel der Olympischen Spiele in London, der Fußball- Europameisterschaft 2012 und der Frankfurter Buchmesse in den nächsten Tagen nachgehen.");
        liste.add("Für die Studierenden des 5. Semesters bietet das ISEM unter Leitung von Prof. Dr. Ursina Böhm, Dipl.-Angl. Sabine Matejek, Prof. Dr. Martin Kornmeier und Prof. Dr. Bernhard Ling nicht nur die Möglichkeit, sich mit dem Seminarthema intensiv und fachübergreifend auseinander zu setzen, sondern auch die Gelegenheit interkulturelles Wissen anzuwenden, Teamwork-, Präsentations- und Recherchefähigkeiten und -techniken zu verbessern sowie internationale Freundschaften zu schließen.");
        liste.add("Am Freitag, 23. November 2012, feierte die Duale Hochschule Baden-Württemberg (DHBW) Mannheim im Mannheimer Rosengarten ab 19.00 Uhr bei der legendären Night of the Graduates in einer rauschenden Ball- und Partynacht mit weit über 6000 Gästen die Ehrung eines der stärksten Absolventenjahrgänge ihrer Geschichte.");
        liste.add("Die DHBW Mannheim ließ es sich nicht nehmen, diesen Erfolg in einer gigantischen Ballnacht zu feiern und die rund 1.700 Absolventen, deren Freunden, Familien, Firmenvertretern, Professoren und Hochschulangehörigen im Mannheimer Rosengarten standesgemäß mit hochkarätigen Liveacts, einem bunten Showprogramm und der Ehrung der Besten zu verabschieden.");
        liste.add("Albrecht Hornbach, Vorstand der Hornbach Holding AG, der bereits zuvor eine Rede vor der Company Lounge, der Ehrenlounge für Firmenvertreter, hielt, in der er über die Herausforderungen eines global agierenden Unternehmens referierte");
        liste.add("Schon zu Beginn des Hoodie-Verkaufs hat sich die Studierendenvertretung darauf festgelegt, einen Teil des Erlöses an das Kinderhospiz Sterntaler zu spenden");
        liste.add("Dass gerade auch vor diesem schwierigen Thema die Studierenden ihre Auge nicht verschließen, liegt Alexandra Rieker besonders am Herzen");
        liste.add("Von 1999 bis 2004 habe ich in Magdeburg Informatik studiert und war dann ein Jahr am Fraunhofer Institut für Autonome Informationssysteme in Sankt Augustin.");

        input.add("hallo");
        input.add("Beginn");
        input.add("Erfolg");

        //databaseText = (LinkedList) liste.clone(); //
        databaseText = PrGenerator.mainDatabase.getCurrentData();

        String textStr = "";
        LinkedList <String> userInputFiltered = new <String> LinkedList(PrGenerator.mainDatabase.getUserInputFiltered());
        findInput(userInputFiltered);
        findPlace();
        findDhRelation();
        findTime();
        selectSentences();

        //shuffeling the final text
        Collections.shuffle(finalText);

        //avoiding of sentence which should not start the text
        while (finalText.getFirst().toString().startsWith("(Sie)|(Er)|(Das)")) {
            Collections.shuffle(finalText);
        }

        //adding all the sentences to a final string
        int counter=0;
        for (Iterator<String> i = finalText.iterator(); i.hasNext();) {
            counter++;
            textStr = textStr +i.next() +" ";
            if (counter==4){
                textStr=textStr+ "<br/> <br/>";
                counter=0;
            }
            
        }

        PrGenerator.mainDatabase.setCreatedText(textStr);  // set final text in the database
        
        
        System.out.println("Final text: " +textStr);
        
        /*for (Iterator<String> i = finalText.iterator(); i.hasNext();) {
            String s=i.next();
            System.out.println(s);
        }
        */
    }
       
    /**
     * method to return a list with founded sentences that match with user input
     * @return preText
     */
    
    public LinkedList<String> getPreText() {
        return preText;
    }
    
    /**
     * method to return a list with founded sentences that have a relation to DHBW
     * @return dhText
     */
    public LinkedList<String> getDhText() {
        return dhText;
    }
    
    /**
     * method to return a list with founded sentences that contain a location/place 
     * @return placeText
     */

    public LinkedList<String> getPlaceText() {
        return placeText;
    }
    
    /**
     *  method to return a list with founded sentences that contain time
     * @return timeText
     */

    public LinkedList<String> getTimeText() {
        return timeText;
    }

    /**
     * method to return a finalText (LinkedList)
     * @return finalText
     */
    
    public LinkedList<String> getfinalText() {
        return finalText;
    }
      
   
}
