/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

/**
 *
 * @author Jörg Woditschka
 */
public class HeadingCreator {
    
    public void createHeading(){
        String[] templateFill= PrGenerator.mainDatabase.getTemplateFill(); //gets filling for the template out of the database
        //String[] templateFill=new String[]{"Biergarten", "heute", "Wettkampf"}; //only for testing 
        String path="/sources/templates_heading.xml"; 
        String heading = "";
        Template templateReader = new Template();
        
        if(templateFill[0] != null){
            heading += templateReader.readXML(path, "template");
            heading = heading.replace("_location_", templateFill[0]);
        }
        if(templateFill[1] != null){
            heading = heading.replace("_date_", templateFill[1]);
        }
        if(templateFill[2] != null){
            heading = heading.replace("_keyAspect_", templateFill[2]);
        }
        
        PrGenerator.mainDatabase.setCreatedHeading(heading); //puts heading into the database
        System.out.println("Überschrift \""+heading+"\" in Database abgelegt.");
    }
    
    public static void main(String args[]){ //only for testing
        HeadingCreator heading = new HeadingCreator();
        heading.createHeading();
    }
}
