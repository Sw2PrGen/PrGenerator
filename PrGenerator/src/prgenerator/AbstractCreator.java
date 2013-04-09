/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.Random;


/**
 *
 * 
 * 
 * 
 * 
 * @author Dominik Künne
 * 
 * Abstractgenerator: Methode createAbstract() funktioniert. Als Eingabe wurde folgende XML-DAtei verwendet:
 * 
     <?xml version="1.0"?>
     <template>
	<templateOrt> Ich war in _location_ . </templateOrt>
	<templateOrt> Ich war nicht in _location_ .</templateOrt>
	<templateOrt> Ich wollte nach _location_ .</templateOrt>
	<templateOrt> Ich möchte gern nach _location_ .</templateOrt>
	<templateOrt> Ich werde nach _location_ gehen.</templateOrt>
	<templateDate> Es war der _date_ .</templateDate>
	<templateDate> Es war am _date_ .</templateDate>
	<templateDate> Es sollte der _date_ sein.</templateDate>
	<templateKeyAspect> Ich war hier um _keyAspect_ zu spielen.</templateKeyAspect>
	<templateKeyAspect> Ich war dort um _keyAspect_ zu spielen.</templateKeyAspect>
     </template>
 * 
 * 
 */


public class AbstractCreator {
    
   
    
    public void createAbstract(String path, String location, String date, String keyAspect){
        
        Template templateReader = new Template();
        String abstractTemplateQuery = "";
        if(location != null){
            abstractTemplateQuery += templateReader.readXML(path, "templateOrt");
            abstractTemplateQuery = abstractTemplateQuery.replace("_location_", location);
        }
        
        if(date != null){
            abstractTemplateQuery += templateReader.readXML(path, "templateDate");
            abstractTemplateQuery = abstractTemplateQuery.replace("_date_", date);
        }
        if (keyAspect != null){
            abstractTemplateQuery += templateReader.readXML(path, "templateKeyAspect");
            abstractTemplateQuery = abstractTemplateQuery.replace("_keyAspect_", keyAspect);
        }
        
        

        System.out.println(abstractTemplateQuery);
       
    }
   public static void main(String [] arg){
       AbstractCreator abstractCreator = new AbstractCreator();
       abstractCreator.createAbstract("d:\\xmlTemplate.xml", "Bad Kreuznach", "12.12.12", "Fussball");
   } 
}



    
    
