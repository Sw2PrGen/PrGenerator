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

    private static void modifyInputtoString() {
        // String rarestring=PrGenerator.mainDatabase.getUserInput(); 
        //rareString=rareString.replace("und", rareString)
        String rareString = "Prof. Dr. Stefan    Bauch Steine im Weg Mannheim auch§$%&/\\";

        if(rareString.equals("Mögliche Suchbegriffe hier eingeben...")){
                      
        }else{
        
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
        rareString = rareString.replaceAll("[^(a-zA-Z)|\\s]", "");
        
        //Deletion of unecessary spaces
        rareString = rareString.replaceAll("\\s\\s+", " ");

//        PrGenerator.mainDatabase.setUserInput(rareString);
        inputStrToList(rareString);
        }
    }

    /**
     * Funtion 
     * @param cleanString clean String(user input String without any special caracters, abreviation, fillwords and unnecessary spaces
     */
    private static void inputStrToList(String cleanString) {
        // String cleanString;
        // cleanString=PrGenerator.mainDatabase.getUserInput();
        String[] cleanStringArray;
        cleanStringArray = cleanString.split(" ");
        LinkedList finalInputList = new <String>LinkedList();

        for (int i = 0; i < cleanStringArray.length; i++) {
            finalInputList.addFirst(cleanStringArray[i]);
        }
        System.out.println(finalInputList);
    }

    public static void main(String[] args) {

        modifyInputtoString();
    }
}
