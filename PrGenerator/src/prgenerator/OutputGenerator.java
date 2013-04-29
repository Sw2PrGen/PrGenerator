package prgenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Katharina Sandrock
 */
public class OutputGenerator {

    private String headingText;
    private String abstractText;
    private String bodyText;
    private String picture;

    /**
     * this method creates out of the generated text an HTML version the
     * structure and the design of the HTML document complies with the DHBW
     * design therefore it takes the created heading, abstract and text and the
     * chosen picture and adds the current date to the press release
     */
    public void generateOutput() {

        headingText = PrGenerator.mainDatabase.getCreatedHeading();
        abstractText = PrGenerator.mainDatabase.getCreatedAbstract();
        bodyText = PrGenerator.mainDatabase.getCreatedText();
        picture = PrGenerator.mainDatabase.getChosenPicture();

        //get the current date
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        Date time = new Date();
        String date = formatDate.format(time);

        //final HTML document designed like the press release of the DHBW with the inserted press release parts
        String finalHtmlDocument = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n<head>\n<title>" + headingText + "</title>\n <style type=\"text/css\"> \n h1 \n{font-size: 14px;\n font-weight: normal;\nline-height: 1.4em;\nmargin: 12px 0; \ntext-transform: uppercase;\n}\nspan.news-list-date {\n    float: right;\n    font-size: 11px;\n    padding-left: 5px\n}\n DIV.csc-textpic-text { \n    clear: both;\n} \nDIV.csc-textpic DIV.csc-textpic-single-image IMG {\n margin-bottom: 10px\n}\n DIV.csc-textpic IMG {\n border: medium none;\n } \nimg {\n border: medium none;\n} \n html, body, table, th, td, div, li {\n font-family: arial,verdana,helvetica,lucida,sans-serif;\n font-size: 12px;\n  line-height: 1.4em;\n}html\n { \n   color: #2F3032;\n}\n</style>\n</head>\n<body>\n<div style=\"margin-right:105px;\">\n<div class=\"csc-textpic-text\">\n <span class=\"news-list-date\">" + date + " </span>\n<h1> " + headingText + "</h1>\n<div class=\"news-single-item\">\n<div class=\"csc-textpic csc-textpic-intext-right-nowrap\">\n<div class=\"csc-textpic-imagewrap csc-textpic-single-image\">\n<div class=\"news-single-img\">\n<img border=\"0\" alt=\"\" src=\"" + picture + "\" align=\"right\">\n<p class=\"news-single-imgcaption\" style=\"width:240px;\"></p>\n</div>\n</div>\n<p><strong>" + abstractText + "</strong></p>\n<p>" + bodyText + "</p>\n</div>\n</div>\n</div>\n<div class=\"csc-textpic-clear\"><!-- --></div>\n</div>\n</body>";

        //write it into the database
        PrGenerator.mainDatabase.setFinalHtmlDocument(finalHtmlDocument);

    }
}
