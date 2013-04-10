/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author rusinda
 */
public class TextCreator extends ArrayList<String>{
 
    
    
    
    
    //variabel for number of found sentences
    private int counter=0; 
     private static LinkedList databasetext =new <String>LinkedList();
    private static LinkedList pretext = new <String>LinkedList();
    private static LinkedList dhtext = new <String>LinkedList();
    private static LinkedList placetext =new <String>LinkedList();
    private static LinkedList timetext =new <String>LinkedList();
    private static LinkedList finaltext=new <String>LinkedList();
    
    
    @Override
    public boolean contains(Object o) {
        String paramStr = (String)o;
        for (String s : this) {
            if (paramStr.equalsIgnoreCase(s)){ return true;}
        }
        return false;
    }

    
    
    
  

    public static void findTime() {
        String current;
      
        int count = 0;
       for(Iterator<String> i=databasetext.iterator();i.hasNext();){
            current = i.next();

            if (current.matches(".*\\s(Montag|Dienstag|Mittwoch|Donnerstag|Freitag|Samstag|Sonnabend|Sonntag|heute|gestern|morgen|Januar|Februar|März|April|Mai|Juni|Juli|August|September|Oktober|November|Dezember|Weihnachten|Ostern|Silvester)\\s.*")) {
                timetext.add(current);
                count = count + 1;
                databasetext.remove(i);
                 if(count>2){break;}
                
            }

           
        }

      

    }

    public static void findPlace() {
        String current;
    
        int count = 0;
       for(Iterator<String> i=databasetext.iterator();i.hasNext();){
            current = i.next();
        //    System.out.println(current);

            if (current.matches(".*\\sin\\s[A-Z].*")) {
                placetext.add(current);
                count = count + 1;
                databasetext.remove(i);
                 if(count>2){break;}
                
            }

          
        }

        
    }

    /**
     *
     * @param liste Liste mit allen Sätzen
     * @return Satz welcher den DH-Bezug sicherstellt
     */
    private static void finddhbezug() {
        String current;
        int counter=0; 
        for(Iterator<String> i=databasetext.iterator();i.hasNext();){
            
            current = i.next();
            if (current.contains("DHBW")) {
                dhtext.add(current);
                databasetext.remove(i);
                counter++;
                if(counter>2){break;}
            }
            if (current.contains("DH")) {
                dhtext.add(current);
                databasetext.remove(i);
                counter++; 
                if(counter>2){break;}
            }
            if (current.contains("Duale Hochschule Baden")) {
               dhtext.add(current);
               databasetext.remove(i);
               counter++;
               if(counter>2){break;}
            }
            
        }
        
    }
    
    private static void findinput( LinkedList input) {
        String current;
        String currentinput; 
        
        while (!input.isEmpty()) {
            
           
            currentinput=(String) input.getFirst(); 
             for(Iterator<String> i=databasetext.iterator();i.hasNext();){
               current=i.next();
               if(current.contains(currentinput)){
                   
                   pretext.add(current);
                   databasetext.remove(i);
             }
            
        }
 input.removeFirst();
       
    }
    
    }
    public static  void createText(){
        
       finaltext.addAll(dhtext);
       finaltext.addAll(timetext);
       finaltext.addAll(placetext);
       for(int i=0; i<20-finaltext.size();i++){
           if(i>pretext.size()){break;}
           finaltext.add(pretext.get(i));
             }
       int randomNumber; 
       for(int i=0; i<20-finaltext.size();i++){
          randomNumber=(int) Math.random()*databasetext.size()+1;
           finaltext.add(databasetext.get(randomNumber));
       }
       
        //System.out.println(finaltext);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here

        // linked list für alle sätze und text für die liste der nachher verwedndeten sätez 
        LinkedList liste = new <String>LinkedList();
        LinkedList text = new <String>LinkedList();
        LinkedList input = new <String>LinkedList();

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
        
        
        input.add("in");
        input.add("hallo");
        input.add("Beginn");
        input.add("Erfolg");
        
        databasetext=(LinkedList) liste.clone();
        //System.out.println(databasetext);
   //   String current; 
    //    for(Iterator<String> i=databasetext.iterator();i.hasNext();){
     //          current=i.next();
       //       System.out.println(current);
       // }
        
        
        findTime();
        findPlace();
        findinput(input); 
        finddhbezug();
      //  System.out.println(dhtext);
        
        createText();
        System.out.println(finaltext);
        
        
    // System.out.println(timetext);
        
        
       //findinput(liste,input);
       //findplace(liste);
       //findtime(liste); 
       
      //  String zeit = findTime(liste);
      //  String ort = findPlace(liste);
        // System.out.print("*+~'#!$%&%&/())?))??=}][{");
       // System.out.println(zeit);
        
      //  System.out.println("hallo".contains("hallo"));
    }  
    
    
}
