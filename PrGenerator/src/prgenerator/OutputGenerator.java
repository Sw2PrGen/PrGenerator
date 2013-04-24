/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author rusinda
 */
public class OutputGenerator {

    private String headingText;
    private String abstractText;
    private String bodyText;
    private String picture;
    

    /*
     * this method creates out of the generated text an html version the
     * structure and the design of the html document complies with the DHBW
     * design
     */
    public void generateOutput() {
        headingText = PrGenerator.mainDatabase.getCreatedHeading();
        abstractText = PrGenerator.mainDatabase.getCreatedAbstract();
        bodyText = PrGenerator.mainDatabase.getCreatedText();
        picture = PrGenerator.mainDatabase.getChosenPicture();
    
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        Date time = new Date();
        String date = formatDate.format(time);
        
        String finalHtmlDocument = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n<head><title>" + headingText + "</title> <style type=\"text/css\">h1 {font-size: 14px; font-weight: normal;line-height: 1.4em;margin: 12px 0; text-transform: uppercase;}span.news-list-date {    float: right;    font-size: 11px;    padding-left: 5px} DIV.csc-textpic-text {     clear: both;} DIV.csc-textpic DIV.csc-textpic-single-image IMG { margin-bottom: 10px} DIV.csc-textpic IMG { border: medium none; } img { border: medium none;} html, body, table, th, td, div, li { font-family: arial,verdana,helvetica,lucida,sans-serif; font-size: 12px;  line-height: 1.4em;}html {    color: #2F3032;}</style></head><body><div style=\"margin-right:105px;\"><div class=\"csc-textpic-text\"> <span class=\"news-list-date\">" + date + " </span><h1> " + headingText + "</h1><div class=\"news-single-item\"><div class=\"csc-textpic csc-textpic-intext-right-nowrap\"><div class=\"csc-textpic-imagewrap csc-textpic-single-image\"><div class=\"news-single-img\"><img border=\"0\" alt=\"\" src=\"" + picture + "\" align=\"right\"><p class=\"news-single-imgcaption\" style=\"width:240px;\"></p><p><strong>" + abstractText + "</strong></p><p>" + bodyText + "</p></div></div></body>";
        PrGenerator.mainDatabase.setFinalHtmlDocument(finalHtmlDocument);
//width=\"240\" height=\"160\"
    }
}
