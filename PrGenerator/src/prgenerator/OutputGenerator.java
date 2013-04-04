/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

/**
 *
 * @author rusinda
 */
public class OutputGenerator {
    
    public static AbstractCreator mainAbstractCreator = new AbstractCreator();
    public static Database mainDatabase = new Database();
    public static Gui mainGui = new Gui();
    public static HeadingCreator mainHeadingCreator = new HeadingCreator();
    public static InputAnalyzer mainInputAnalyzer = new InputAnalyzer();
    public static OutputGenerator mainOutputGenerator = new OutputGenerator();
    public static PictureChooser mainPictureChooser = new PictureChooser();
    public static TextCreator mainTextCreator = new TextCreator();
    
}
