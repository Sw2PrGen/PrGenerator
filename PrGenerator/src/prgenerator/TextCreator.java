/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 * responsible for creating of main text
 *
 * @author Stefan Dreher, Yuliya Kuznetsova
 */
public class TextCreator {

    private static LinkedList databaseText = new <String>LinkedList(); // PrGenerator.mainDatabase.get...copy of the date from database
    private LinkedList preText = new <String>LinkedList();     // list for founded sentences that match with user input
    private LinkedList dhText = new <String>LinkedList();      // list for founded sentences that have a relation to DHBW
    private LinkedList placeText = new <String>LinkedList();    // list for founded sentences that contain a location/place 
    private LinkedList timeText = new <String>LinkedList();     // list for founded sentences that contain time
    private LinkedList finalText = new <String>LinkedList();     // finalText

    /**
     * Method to find a sentence with a relation to a special time
     *
     */
    private void findTime() {

        String current;      // temporary String for the current list element
        int count = 0;      //  counter for number of founded sentences    
        Collections.shuffle(databaseText);
        //iteration trough the whole database
        for (Iterator<String> i = databaseText.iterator(); i.hasNext();) {
            current = i.next();
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
        for (Iterator<String> i = timeText.iterator(); i.hasNext();) {
            String s = i.next();
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

        for (Iterator<String> it = placeText.iterator(); it.hasNext();) {
            String s = it.next();
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
        while (i.hasNext()) {
            current = i.next();
            if (current.contains("DHBW")) {
                dhText.add(current);
                i.remove(); //deletion of found sentence to avoid doubling
                counter++;
                if (counter > 2) {
                    break; // condition to break the loop if more than 2 sentences are found
                }
            } else if (current.contains("DH")) {
                dhText.add(current);
                i.remove(); //deletion of found sentence to avoid doubling
                counter++;
                if (counter > 2) {
                    break; // condition to break the loop if more than 2 sentences are found
                }
            } else if (current.contains("Duale Hochschule Baden")) {
                dhText.add(current);
                i.remove(); //deletion of found sentence to avoid doubling
                counter++;
                if (counter > 2) {
                    break; // condition to break the loop if more than 2 sentences are found
                }
            }
        }

        for (Iterator<String> it = dhText.iterator(); it.hasNext();) {
            String s = it.next();
        }
    }

    /**
     * Method to find sentences related to the user input
     *
     * @param input filtered user input
     */
    private void findInput(LinkedList input) {
        String current; //current sentence        
        String currentinput; //current user input
        Collections.shuffle(databaseText);

        //iteration through the list to find all sentences with a relation to the user input
        while (!input.isEmpty()) {
            currentinput = " " + (String) input.getFirst().toString().toLowerCase() + " ";
            Iterator<String> i = databaseText.iterator();
            while (i.hasNext()) {
                current = i.next();
                if (current.toString().toLowerCase().contains(currentinput)) {  // check for matches of user input with current sentence from the database (both uncapitalized)
                    preText.add(current);
                    i.remove(); //deletion of found sentence to avoid doubling
                }
            }
            input.removeFirst();
        }
        for (Iterator<String> it = preText.iterator(); it.hasNext();) {
            String s = it.next();
        }
        if (preText.isEmpty() && !PrGenerator.mainDatabase.getUserInputFiltered().isEmpty()) {
            JOptionPane.showConfirmDialog(null, "Input wurde nicht gefunden. Es wurde ein zufälliger Text generiert", "Achtung", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);// JOptionPane.WARNING_MESSAGE);
        }

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
        for (int i = 2; i < 21 - finalText.size(); i++) {
            if (i >= preText.size()) {
                break;
            }
            finalText.add(preText.get(i));
        }
        int randomsize = (int) (Math.random() * 10);
        int randomNumber;
        //optional filling of the text with random sentences if there where not found enough sentences related to the user input
        for (int i = 0; i < 10 + randomsize - finalText.size(); i++) {
            randomNumber = (int) (Math.random() * (databaseText.size() + 1));
            finalText.add(databaseText.get(randomNumber));
        }
    }

    /**
     *
     * Method for creating the main text of a programm. Invokes findTime(),
     * findPlace(), findDhRelation(), findInput() and selectSentences(), then
     * shuffles the final text to get random order of sentences
     *
     */
    public void createMainText() {
        finalText.clear();
        preText.clear();
        dhText.clear();
        timeText.clear();
        placeText.clear();
        databaseText = PrGenerator.mainDatabase.getCurrentData();
        String textStr = "";
        LinkedList<String> userInputFiltered = new <String> LinkedList(PrGenerator.mainDatabase.getUserInputFiltered());
        findInput(userInputFiltered);
        findPlace();
        findDhRelation();
        findTime();
        selectSentences();

        //avoiding of sentence which should not start the text
        while (finalText.getFirst().toString().startsWith("(Sie)|(Er)|(Das)")) {
            Collections.shuffle(finalText);
        }

        if (!preText.isEmpty()) {
            finalText.addFirst(preText.getFirst());

        }
        //adding all the sentences to a final string
        int counter = 0;
        for (Iterator<String> i = finalText.iterator(); i.hasNext();) {
            counter++;
            textStr = textStr + i.next() + " ";
            if (counter == 4) {
                textStr = textStr + "<br/> <br/>";
                counter = 0;
            }
        }
        PrGenerator.mainDatabase.setCreatedText(textStr);  // set final text in the database 
    }
}
