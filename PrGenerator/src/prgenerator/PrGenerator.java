/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import javax.swing.JOptionPane;

/**
 * Main Class of the project, holds static instances of all classes needed and
 * manages the control flow of the application
 *
 * @author Dawid Rusin
 */
public class PrGenerator {

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
        mainDatabase = new Database();              //create all objects required
        mainAbstractCreator = new AbstractCreator();
        mainHeadingCreator = new HeadingCreator();
        mainInputAnalyzer = new InputAnalyzer();
        mainOutputGenerator = new OutputGenerator();
        mainPictureChooser = new PictureChooser();
        mainTextCreator = new TextCreator();

        doit();         //start the main loop
    }

    /**
     * Main loop of the application, steers the control flow
     */
    public static void doit() {
        mainGui.start();            //GUI is needed as a thread to enable better user experience
        do {
            while (mainDatabase.getUserInput() == null) {       //wait fot the userInput to be set
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {             //something, somewhere went horribly wrong
                    JOptionPane.showConfirmDialog(null, "Kritischer Fehler! Bitte Programm neustarten.", "Kritischer Fehler", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                }
            }

            //main control flow
            PrGenerator.mainInputAnalyzer.modifyInputToString();
            PrGenerator.mainDatabase.manageData();
            PrGenerator.mainTextCreator.createMainText();
            PrGenerator.mainAbstractCreator.createAbstract();
            PrGenerator.mainHeadingCreator.createHeading();
            PrGenerator.mainPictureChooser.choosePicture();
            PrGenerator.mainOutputGenerator.generateOutput();
            while (mainDatabase.getUserInput() != null) {       //on this point userInput will be reset by pressing "zurück" or the programm will be closed
                try {
                    Thread.sleep(100);      //wait for the userinput to be reset to start over again
                } catch (InterruptedException ex) {             //something, somewhere went horribly wrong
                    JOptionPane.showConfirmDialog(null, "Kritischer Fehler! Bitte Programm neustarten.", "Kritischer Fehler", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                }
            }
        } while (true);

    }
}