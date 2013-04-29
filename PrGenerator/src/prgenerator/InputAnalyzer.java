/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.util.LinkedList;

/**
 *
 * @author Stefan Dreher
 */
public class InputAnalyzer {

    
    /**
     * Method to filter the userinput to get the best result when searching for the input
     * 
     */
    public static void modifyInputToString() {
       //Assign rareStirng the unfiltered userinput from the database to filter it 
        
        String rareString = PrGenerator.mainDatabase.getUserInput();
   

        if (rareString.equals(PrGenerator.mainDatabase.getSEARCH_DEFAULT()) || rareString.isEmpty()) {
            clearInput();
        } else {

            //Deletion of abreviations
            rareString = rareString.replaceAll("allg\\.", "");
            rareString = rareString.replaceAll("bzw\\.", "");
            rareString = rareString.replaceAll("bspw\\.", "");
            rareString = rareString.replaceAll("d\\.h\\.", "");
            rareString = rareString.replaceAll("etc\\.", "");
            rareString = rareString.replaceAll("usw\\.", "");
            rareString = rareString.replaceAll("geb\\.", "");
            rareString = rareString.replaceAll("s\\.", "");
            rareString = rareString.replaceAll("od\\.", "");
            rareString = rareString.replaceAll("s\\.a\\.", "");
            rareString = rareString.replaceAll("u\\.", "");
            rareString = rareString.replaceAll("u\\.a\\.", "");
            rareString = rareString.replaceAll("u\\.Ä\\.", "");
            rareString = rareString.replaceAll("v\\.a\\.", "");
            rareString = rareString.replaceAll("vgl\\.", "");
            rareString = rareString.replaceAll("Prof\\.", "");
            rareString = rareString.replaceAll("Dr\\.", "");
            rareString = rareString.replaceAll("Dpl\\.", "");
            rareString = rareString.replaceAll("Mr\\.", "");
            rareString = rareString.replaceAll("Ms\\.", "");
            rareString = rareString.replaceAll("Anh\\.", "");
            rareString = rareString.replaceAll("Bd\\.", "");
            rareString = rareString.replaceAll("dgl\\.", "");
            rareString = rareString.replaceAll("d\\.h\\.", "");
            rareString = rareString.replaceAll("vlt\\.", "");
            rareString = rareString.replaceAll("gem\\.", "");
            rareString = rareString.replaceAll("Nr\\.", "");
            rareString = rareString.replaceAll("zz\\.", "");





            //Deletion of unnecessary words      
            rareString = rareString.replaceAll("(?i)\\bund\\b", " ");
            rareString = rareString.replaceAll("(?i)\\baber\\b", " ");
            rareString = rareString.replaceAll("(?i)\\boder\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bmit\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bein\\b", " ");
            rareString = rareString.replaceAll("(?i)\\beine\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bohne\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bwas\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bauf\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bunter\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bzwischen\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bin\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bauch\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bim\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bweil\\b", " ");
            rareString = rareString.replaceAll("(?i)\\bwegen\\b", " ");

            //Deletion of special characters
            rareString = rareString.replaceAll("[^a-zA-Z|\\s]", "");

            //Deletion of unecessary spaces
            rareString = rareString.replaceAll("\\s\\s+", " ");
           
            //calling the method to put the input in a list
            inputStrToList(rareString);
        }
    }

    private static void clearInput(){
                 LinkedList finalInputList = new <String>LinkedList();

        finalInputList.clear();
            PrGenerator.mainDatabase.setUserInputFiltered(finalInputList);
    }
    
    
    /**
     * Method to transform the filtered input from String to a linkedList
     *
     * @param cleanString clean String(user input String without any special
     * caracters, abreviation, fillwords and unnecessary spaces
     */
    private static void inputStrToList(String cleanString) {
        
       
        //deletion of blanks at beginning or end of String
        if(cleanString.startsWith(" ")){
            cleanString=cleanString.substring(1);
        }
        
        
        //temporary linked list with userInput
        LinkedList finalInputList = new <String>LinkedList();
       
        if(cleanString.equalsIgnoreCase(" ") || cleanString.isEmpty()){
            clearInput();
        }else{
        //checking if there are more than one word
        if(cleanString.contains(" ")){
            
            
       //Array for the splitted String
        String[] cleanStringArray;
        
        //splitting the input string into an array
        cleanStringArray = cleanString.split(" ");
       
        
        
        //filling the list with the array-content
        for (int i = 0; i < cleanStringArray.length; i++) {
            finalInputList.addFirst(cleanStringArray[i].replaceAll(" ",""));
        }
        PrGenerator.mainDatabase.setUserInputFiltered(finalInputList);

        }
        else {
            //adding the only word
            
            finalInputList.add(cleanString);
                   
            //set the userinput
            PrGenerator.mainDatabase.setUserInputFiltered(finalInputList);
            
        }
        
        }
        

    }

    public static void main(String[] args) {

    }
}
