/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.io.File;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Dominik Künne
 */
class Template{
    public Template(){
        
    }
    
    public String readXML(String path, String tagName){
        try {
 
	File fXmlFile = new File(path);
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
 

	doc.getDocumentElement().normalize();
 
	NodeList nList = doc.getElementsByTagName(tagName);
        
        Random generator = new Random();
        int length = nList.getLength();
        int i = length-1 - generator.nextInt(length);
        
        Node nNode = nList.item(i);
	
	return nNode.getTextContent();
	
    } catch (Exception e) {
	e.printStackTrace();
    }
        return null;
  }

}
    