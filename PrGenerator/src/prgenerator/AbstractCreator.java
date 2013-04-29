package prgenerator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Objects of this class can create abstracts. The class contains two main
 * methods, the "analyzeText()" and the "createAbstract()" methods, where
 * "createAbstract" is called for creating an abstract.
 *
 * @author Dominik Künne, Jörg Woditschka, Tobias Mauritz
 */
public class AbstractCreator {

    private LinkedList<String> whenWL = new LinkedList<>(); // when word list
    private LinkedList<Integer> whenCL = new LinkedList<>(); // when couter list
    private LinkedList<String> whereWL = new LinkedList<>();
    private LinkedList<Integer> whereCL = new LinkedList<>();
    private LinkedList<String> whatWL = new LinkedList<>();
    private LinkedList<Integer> whatCL = new LinkedList<>();
    private String maintext;// = PrGenerator.mainDatabase.getCreatedText();

    /**
     * stores the words into WL and increases its counter in CL
     *
     * @author Tobias Mauritz
     * @param sWord word which should be stored into the word list WL
     * @param WL one of the three word lists (whereWL, whenWL, whatWL)
     * @param CL one of the three counter list, how often a word is occured
     * (whereCL, whenCL, whatCL)
     *
     */
    public void storeWord(String sWord, List WL, List CL) {
        if (WL.contains(sWord)) {
            int i = WL.indexOf(sWord);          // als Wahrheitswert benutzen?
            int counter = (Integer) CL.get(i);  // current value of counter
            counter++;  // inc counter
            CL.remove(i);       // delete list object at index i
            CL.add(i, counter); // add list object at index i with new counter value
            // old list object has to be removed because with "add" it would not be overwriten 
        } else {
            // adds new word and sets counter to 1
            WL.add(sWord);
            CL.add(1);
        }
    }

    /**
     * determines the index of the most used word within the list
     *
     * @author Tobias Mauritz
     * @param CL the counter list
     * @return the id within the list with the highest counter
     */
    public int getWordIndex(List CL) {
        int min = 0;
        int idx = 0;

        // looks from last element of the list if the counter_new >= counter_old
        // so the word which was first found and is mostly used will be applied
        for (int i = CL.size() - 1; i >= 0; i--) {
            if ((Integer) CL.get(i) >= min) {
                min = (Integer) CL.get(i);
                idx = i;
            }
        }
        return idx;
    }

    /**
     * determines the index of the what word
     *
     * @author Tobias Mauritz
     * @param WL the word list
     * @return the id within the list
     */
    public int getWord(List WL) {
        int idx = -1;
        String inputwords = PrGenerator.mainDatabase.getUserInput();
        String currentword;

        // if a word from users input is within the what word list this is taken
        for (int i = WL.size() - 1; i >= 0; i--) {
            currentword = (String) WL.get(i);
            if (inputwords.contains(currentword)) {
                idx = i;
            }
        }
        // if not the mostly used word with capital letter is taken
        if (idx == -1) {
            idx = getWordIndex(whatCL);
        }
        return idx;
    }

    /**
     * analyzes the text for the three questions where, when, what with the word
     * and counter lists
     *
     * @author Tobias Mauritz
     */
    public void analyzeText() {
        maintext = PrGenerator.mainDatabase.getCreatedText();

        maintext = maintext.replaceAll("([0-9]{1,2})\\. ", "$1.");   // alle "ZAHL. " in "ZAHL." umwandeln
        maintext = maintext.replaceAll("\\. ", " ");                 // alle ". " in " " umwandeln
        maintext = maintext.replaceAll("([0-9]{1,2})\\.", "$1. ");   // alle "ZAHL." in "ZAHL. " umwandeln
        maintext = maintext.replaceAll("!", "");
        maintext = maintext.replaceAll("\\?", "");
        maintext = maintext.replaceAll(";", "");
        maintext = maintext.replaceAll(",", "");

        LinkedList<String> words = new LinkedList<>(Arrays.asList(maintext.split(" ")));
        int txtLength = words.size();

        for (int i = 0; i < txtLength; i++) {
            String nWord = words.get(i);

            //modified by Jörg: store dates needing an "am" 
            if (nWord.matches("(Montag|Dienstag|Mittwoch|Donnerstag|Freitag|Samstag|Sonnabend|Sonntag)")) {
                storeWord("am " + nWord, whenWL, whenCL);

                //modified by Jörg: store dates which can be inserted without an "am"
            } else if (nWord.matches("(heute|gestern|morgen|Weihnachten|Ostern|Silvester)")) {
                storeWord(nWord, whenWL, whenCL);

            } else if (nWord.matches("(Januar|Februar|März|April|Mai|Juni|Juli|August|September|Oktober|November|Dezember)")) {

                // add number of day within the month 
                if (i - 1 >= 0) { // if month is at the beginning of the text
                    if (words.get(i - 1).matches("[0-9]{1,2}\\.")) {
                        nWord = "am " + words.get(i - 1) + " " + nWord;
                    }
                }

                // add year
                if (i + 1 < txtLength) { // if end of text is reached with the month
                    if (words.get(i + 1).matches("[0-9]{1,4}")) {
                        nWord = nWord + " " + words.get(i + 1);
                    }
                }
                storeWord(nWord, whenWL, whenCL); // store word with date word, WL, CL

            } else if (nWord.matches("(Woche|Wochen)")) {
                if (i - 1 >= 0) {
                    if (words.get(i - 1).matches("[0-9]{1,2}|einer|zwei|drei|vier|fünf|sechs|sieben|acht|neun|zehn")) {
                        nWord = words.get(i - 1) + " " + nWord;
                        storeWord(nWord, whenWL, whenCL);
                    }
                }

                // stores locations    
            } else if (nWord.matches("in|aus")) {
                if (i + 1 < txtLength) {
                    if (words.get(i + 1).matches("[A-Z]{1,}[a-z]{1,}") && !nWord.matches("Versehen|Bearbeitung|Betrieb")) {
                        nWord = words.get(i + 1);
                        storeWord(nWord, whereWL, whereCL);
                        i++;    // if there was a Noun after "in,aus" (maybe a location) 
                        // this will not saved into the list with words with capital letter
                    }
                }

                // "what" words: stores words which stat with a capital letter
            } else if (nWord.matches("[A-Z]{1,}.{3,}") && !nWord.matches("Duale|Dualen|Hochschule|DHBW|Mannheim|Baden-Württemberg|Studiums|Prof|Ein|Nach|Außerdem|Dies|Jahr|Jahre|Auswirkungen|Fragen")) {
                storeWord(nWord, whatWL, whatCL);
            }

        }

        int whereidx = getWordIndex(whereCL);
        int whenidx = getWordIndex(whenCL);
        int whatidx = getWord(whatWL);

        String[] awords = {whereWL.get(whereidx), whenWL.get(whenidx), whatWL.get(whatidx)};

        // saves the three words Where, When, What into Database
        PrGenerator.mainDatabase.setTemplateFill(awords);
    }

    /**
     * This method generates an abstract and hands it to the database to store
     * it. At the very beginning it starts the "analyzeText()" method, which
     * stores filling words in the database. Matching templates are picked,
     * modified and filled with the picked filling words. At the end the created
     * abstract is handed to the database to store it.
     *
     * @author Dominik Künne, Jörg Woditschka
     */
    public void createAbstract() {
        analyzeText();

        String path = "data/templates_abstract.xml";
        String location = PrGenerator.mainDatabase.getTemplateFill()[0];
        String date = PrGenerator.mainDatabase.getTemplateFill()[1];
        String keyAspect = PrGenerator.mainDatabase.getTemplateFill()[2];

        String abstractTemplateQuery = ""; //String to be returned as abstract
        Template template = new Template();
        Random generator = new Random(); // a random number generator helps picking sentences for the abstract   

        String nextType = "DHBW"; //the first template should be of type "None" or "DHBW"
        int nonCounter = 0;
        LinkedList<String> availableTagNames = new LinkedList(); //list of available TagNames. Get's reduced during the picking.
        availableTagNames.add("templateLocation");
        availableTagNames.add("templateDate");
        availableTagNames.add("templateKeyAspect");

        //this loop adds templates to the abstractTemplateQuery to create an abstract
        for (int i = 0; i < 3; i++) {

            //picking a TagName
            int pickedTagName = generator.nextInt(availableTagNames.size());
            String tagName = availableTagNames.get(pickedTagName);
            availableTagNames.remove(pickedTagName);

            //add another tag to the TagName 
            if (availableTagNames.size() > 1 && generator.nextBoolean()) {
                int pickedTagName2 = generator.nextInt(availableTagNames.size());
                String tagName2 = availableTagNames.get(pickedTagName2);
                availableTagNames.remove(pickedTagName2);
                tagName = (pickedTagName > pickedTagName2) ? tagName2 + tagName.substring(8) : tagName + tagName2.substring(8);
                i++;
            }

            //pick a Type and generate abstractTemplateQuery
            if (generator.nextInt(2) == 0 && nonCounter < 2) {
                abstractTemplateQuery += template.readXML(path, tagName, "None");
                nonCounter++;
                nextType = "DHBW";
            } else {
                abstractTemplateQuery += template.readXML(path, tagName, nextType);
                nextType = (nextType.equals("DHBW")) ? "Sie" : "DHBW";
            }
        }

        //making the first letter capital after a "." or at the beginning of the abstract eg. "am" -> "Am" & "heute" -> "Heute"
        if (abstractTemplateQuery.indexOf("_date_") == 1 || (abstractTemplateQuery.indexOf("_date_") > 1 && abstractTemplateQuery.charAt(abstractTemplateQuery.indexOf("_date_") - 2) == '.')) {
            date = date.substring(0, 1).toUpperCase() + date.substring(1);
        }

        //replace place holders in abstractTemplateQuery
        abstractTemplateQuery = abstractTemplateQuery.replace("_location_", location).replace("_date_", date).replace("_keyAspect_", keyAspect);

        //store the abstract in the database
        PrGenerator.mainDatabase.setCreatedAbstract(abstractTemplateQuery);
    }
}
