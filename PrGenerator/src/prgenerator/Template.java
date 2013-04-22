/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Dominik Künne
 */
class Template {

    public Template() {
    }

    public String readXML(String path, String tagName, String type) {
        try {
            
            
        
        URL uri = getClass().getResource(path);
       
        String filePath = uri.getPath().toString();

        //filePath = filePath.replace("/", "\\");
        
	
        File fXmlFile = new File(filePath);
        
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
        


            doc.getDocumentElement().normalize();

            Element typeNode = (Element) doc.getElementsByTagName(type).item(0);
            NodeList nList = typeNode.getElementsByTagName(tagName);
            System.out.println(tagName);
            Random generator = new Random();
            int length = nList.getLength();
            int i = length - 1 - generator.nextInt(length);

            Node nNode = nList.item(i);
            return nNode.getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
  }
    /*
     public static void main(String [ ] args)
{
    Template test = new Template();
    test.readXML("/sources/templates_abstract.xml", "templateLocation");
     
}   */

}
