/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

/**
 *
 * @author rusinda
 */
public class PrGenerator {
    
    public static Gui mainGui;
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
        mainGui = new Gui();
        
        mainDatabase.manageData();
        // mainPictureChooser.choosePicture();
                
        //   mainTextCreator.createMainText();  
        
//        mainOutputGenerator.generateOutput();
//        mainGui.showResult(); 
        
       // mainInputAnalyzer.modifyInputtoString();
    }
}