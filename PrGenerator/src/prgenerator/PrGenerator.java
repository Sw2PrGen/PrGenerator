/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rusinda
 */
public class PrGenerator {
    
//    public static Gui mainGui;
    public static Thread mainGui = new Thread(new Gui());
    public static AbstractCreator mainAbstractCreator;
    public static Database mainDatabase;
    public static HeadingCreator mainHeadingCreator;
    public static InputAnalyzer mainInputAnalyzer;
    public static OutputGenerator mainOutputGenerator;
    public static PictureChooser mainPictureChooser;
    public static TextCreator mainTextCreator;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mainDatabase = new Database();
        mainAbstractCreator = new AbstractCreator();
        mainHeadingCreator = new HeadingCreator();
        mainInputAnalyzer = new InputAnalyzer();
        mainOutputGenerator = new OutputGenerator();
        mainPictureChooser = new PictureChooser();
        mainTextCreator = new TextCreator();
//        mainGui = new Gui();
//        
        
        doit();
        //mainDatabase.manageData();
        // mainPictureChooser.choosePicture();
                
        //   mainTextCreator.createMainText();  
        
//        mainOutputGenerator.generateOutput();
//        mainGui.showResult(); 
        
       // mainInputAnalyzer.modifyInputtoString();
    }
    
    public static void doit(){
        mainGui.start();
        do {
        while (mainDatabase.getUserInput() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                //Logger.getLogger(PrGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        PrGenerator.mainInputAnalyzer.modifyInputToString();
        PrGenerator.mainDatabase.manageData();
        PrGenerator.mainTextCreator.createMainText();
        PrGenerator.mainAbstractCreator.createAbstract();
        PrGenerator.mainHeadingCreator.createHeading();
        PrGenerator.mainPictureChooser.choosePicture();
        PrGenerator.mainOutputGenerator.generateOutput();
        while (mainDatabase.getUserInput() != null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                //Logger.getLogger(PrGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        } while (true);
       // mainGui.showResult();
        
    }

}