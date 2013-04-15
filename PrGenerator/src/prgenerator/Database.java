/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.*;

/**
 *
 * @author rusinda
 */
public class Database {

    private final String DHBW_URL = "http://www.dhbw-mannheim.de/";
    private final String DHBW_AKTUELLES_URL = "http://www.dhbw-mannheim.de/aktuelles/details/id/";
    private final String DHBW_MORE_TAG = "<span class=\"more\">";
    private final String DHBW_NEWURL_START = "details/id/";
    private final String DHBW_NEWURL_END = "/\" title";
    private int latestUrlNo;
    private int lastLatestUrlNo = 0;
    private LinkedList<String> currentData = new LinkedList<>();
    private String userInput;
    private String createdText;
    private String createdAbstract;
    private String createdHeading;
    private LinkedList<Template> templatesAbstract = new LinkedList<>();
    private LinkedList<Template> templatesHeading = new LinkedList<>();
    private String finalDocument;
    private String finalHtmlDocument;
    private LinkedList<String> pictureList = new LinkedList<>();
    private LinkedList<String> userInputFiltered = new LinkedList<>();
    private String[] templateFill = new String[3];
    private String chosenPicture;
    
    private LinkedList<String> makelist(String input) {
        LinkedList<String> help = new LinkedList<>(Arrays.asList(input.split("[.]")));
        LinkedList<Integer> del = new LinkedList<>();
        for (int i = 0; i < help.size(); i++) {
            help.set(i, help.get(i).trim());
            if (help.get(i).contains("(at)") | help.get(i).length() < 6 | help.get(i).contains("#i#") | help.get(i).contains("#/i#")) {
                del.add(i);
            } else {
                help.set(i, help.get(i).replaceAll("#","\\.") + ".");
            }
        }
        for (int i = 0; i < del.size(); i++) {
            help.remove(del.get(i) - i);
        }
        return help;
    }
    
    /**
     * reads a file
     *
     * @param filename name of the file to read from
     * @return chained list of chained lists with the content of the rows
     * @throws Exception
     */
    static public LinkedList<LinkedList> readFile(String filename) throws Exception {

        LinkedList<LinkedList> list = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String dataRow = reader.readLine();
        while (dataRow != null) {           //runs until it gets to an empty row
            LinkedList<String> strList = new LinkedList<>();
            String[] dataArray = dataRow.split("\n");      //the row is split on the separator and put into an array
            strList.addAll(Arrays.asList(dataArray));   //the array is loaded into a string list
            list.add(strList);                  //the list with the strings is added to the main linked list

            dataRow = reader.readLine(); // Read next line of data.
        }
        // Close the file once all data has been read.
        reader.close();

        return list;
    }

    /**
     * Writes a file on the hdd
     *
     * @param text the content of the file to write
     * @param filename the name of the file
     * @throws Exception
     */
    private void writeFile(String text, String filename) throws Exception {
        String[] str = filename.split("/");
        String dirs = new String();
        for (int i = 0; i < str.length - 1; i++) {
            dirs += str[i] + "/";
        }
        File file = new File(dirs);
        file.mkdirs();
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filename));
        fileWriter.write(text);
        fileWriter.close();
    }

    /**
     * Gets the url of the next news item
     *
     * @param url url to the current news item as String
     * @return a String containing the url to the next news item
     */
    private String getNextUrl(String url) {
        int i = Integer.parseInt(url.split("/")[url.split("/").length - 1]) - 1;    //get the last part of the url (is a 4-digit number), parse it to int and decrement
        while (i >= lastLatestUrlNo) {    //I don't believe there should be negative numbers in the url
            String nexturl = Integer.toString(i); //parse the new number to string
            if (!checkWebsite(DHBW_AKTUELLES_URL + nexturl)) {  //check if the site exists
                i--;                //decrement if does not exist
            } else {
                return DHBW_AKTUELLES_URL + nexturl;    //return the new url if site does exist
            }
        }
        return null;
    }

    /**
     * Checks if a web site exists (faster then getWebsite())
     *
     * @param adress the url to the web site
     * @return true if it exists, false otherwise
     */
    private boolean checkWebsite(String adress) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(adress).openConnection();   //Connect to the site
            con.setRequestMethod("HEAD");                                       //only need header to check if site exists
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);        //true if site exists, false otherwise
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Opens a website and returns a reader to read HTML
     *
     * @param adress the url to the web site as a String
     * @return a Buffered Reader with the content of the web site
     */
    private BufferedReader getWebsite(String adress) {
        BufferedReader reader = null;
        try {
            URL url = new URL(adress);
            URLConnection connection = url.openConnection();    //connect to the web site
            InputStream is = connection.getInputStream();       //get an input stream to read html
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Exception ex) {
            //ex.printStackTrace();
            return null;
        }
        return reader;
    }

    /**
     * Checks the DHBW-Site for the url to the latest news item
     *
     * @return string with the url to the latest news item
     */
    private String getLatestNewsUrl() {
        String s = "";
        BufferedReader reader = getWebsite(DHBW_URL);       //go tho the dhbw-homepage
        try {
            while (s != null & !s.contains(DHBW_MORE_TAG)) {    //this tag is used to display news items
                s = reader.readLine();
            }
            if (s == null) {
                return s;
            }
            s = s.substring(s.indexOf(DHBW_NEWURL_START) + DHBW_NEWURL_START.length(), s.indexOf(DHBW_NEWURL_END)); //get the last part of the url to the news item
            latestUrlNo = Integer.parseInt(s);
            return DHBW_AKTUELLES_URL + s;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;    //return null if sth went wrong
    }

    private String replaceSpecPattern(String string, String regex, String lookFor, String replaceWith) {
        Pattern pattern = Pattern.compile(regex);                   //user pattern to save "." from dates
        Matcher matcher = pattern.matcher(string);
        String help;
        while (matcher.find()) {                                          //is it a date?
            help = matcher.group(matcher.groupCount());
            string = string.replace(help, help.replaceAll(lookFor, replaceWith));            //replace . with #
        }
        return string;
    }

    /**
     * Reads a text from a buffered reader and strips it out of HTML-tags
     *
     * @param reader The buffered reader containing the text to process
     * @return a string with the processed text
     */
    private String getText(BufferedReader reader) {
        String finishedText = "";
        String s = "";
        StringBuilder sb = new StringBuilder();
        while (s != null && !s.contains("<div class=\"csc-textpic-text\">")) {  //go to the text passage in the html-code
            try {
                s = reader.readLine();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        try {
            while ((s = reader.readLine()) != null && !s.contains("</div>")) {  //read all until the end of the text passage
                if(s.contains("www") | s.contains("<a>") | s.contains("<i>") | s.contains("<td ")){
                    continue;
                }
                s = s.replaceAll("\t", "");                                     //delete all tabulators
                s = s.replaceAll("<(\"[^\"]*\"|'[^']*'|[^'\">])*>", "");        //delete all html-tags
                s = s.replaceAll("&nbsp;", "");                                 //delete "&nbsp;"
                s = s.replaceAll(".+[^(. )]$", "");                             //delete every codeline that does not conclude with a full stop
                s = s.replaceAll("„", "");
                s = s.replaceAll("“", "");
                s = s.replaceAll("&amp;", "&");
                s = s.replaceAll("&quot;", "");

                s = replaceSpecPattern(s, "\\d+\\.", "\\.", "#");               //replace all . in dates
                s = replaceSpecPattern(s, " [a-zA-Z]{1,3}\\.", "\\.", "#");     //replace all . in 
                s = replaceSpecPattern(s, "#[a-zA-Z]{1,3}\\.", "\\.", "#");
                s = replaceSpecPattern(s, "Prof\\.", "\\.", "#");
                s = replaceSpecPattern(s, "Dipl\\.", "\\.", "#");
                s = replaceSpecPattern(s, "Angl\\.", "\\.", "#");
                s = replaceSpecPattern(s, "bspw\\.", "\\.", "#");
                
                

                s = s.trim();
                sb.append(s);                                                   //append to the stringbuilder
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        finishedText = sb.toString();                                           //make a string out of the stringbuilder
        return finishedText;
    }

    /**
     *
     * @return
     */
    private boolean loadNewData() {
        LinkedList<String> backupFile = null;
        try{
            
        }catch(Exception e){
            e.printStackTrace();
        }
        String latestUrl = getLatestNewsUrl();      //get the url to the latest news item
        if (latestUrl == null) {
            return false;               //did not work if null, probably no connection
        }
        currentData.addAll(makelist(getText(getWebsite(latestUrl))));
        for(int i = 0; i< 100; i++){
            latestUrl = getNextUrl(latestUrl);
            currentData.addAll(makelist(getText(getWebsite(latestUrl))));
        }
        try{
            StringBuilder s = new StringBuilder();
            for(int i = 0; i< currentData.size(); i++){
                s.append(currentData.get(i));
                s.append("\n");
            }
            writeFile(s.toString(), "testest.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("fertig");

        return true;
    }

    /**
     *
     * @return
     */
    private boolean loadBackup() {
        return true;
    }

    /**
     *
     * @return
     */
    private boolean updateBackup() {
        return true;
    }

    /**
     *
     * @return
     */
    private boolean isLoaded() {
        return true;
    }

    /**
     * Tries to pull new data from the DHBW-Site and update the backup
     * afterwards, loads the backup otherwise
     */
    public void manageData() {
        loadNewData();      //try to get new data
    }

    /**
     * @return the currentData
     */
    public LinkedList<String> getCurrentData() {
        return currentData;
    }

    /**
     * @return the userInput
     */
    public String getUserInput() {
        return userInput;
    }

    /**
     * @param userInput the userInput to set
     */
    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    /**
     * @return the createdText
     */
    public String getCreatedText() {
        return createdText;
    }

    /**
     * @param createdText the createdText to set
     */
    public void setCreatedText(String createdText) {
        this.createdText = createdText;
    }

    /**
     * @return the createdAbstract
     */
    public String getCreatedAbstract() {
        return createdAbstract;
    }

    /**
     * @param createdAbstract the createdAbstract to set
     */
    public void setCreatedAbstract(String createdAbstract) {
        this.createdAbstract = createdAbstract;
    }

    /**
     * @return the createdHeading
     */
    public String getCreatedHeading() {
        return createdHeading;
    }

    /**
     * @param createdHeading the createdHeading to set
     */
    public void setCreatedHeading(String createdHeading) {
        this.createdHeading = createdHeading;
    }

    /**
     * @return the templatesAbstract
     */
    public LinkedList<Template> getTemplatesAbstract() {
        return templatesAbstract;
    }

    /**
     * @param templatesAbstract the templatesAbstract to set
     */
    public void setTemplatesAbstract(LinkedList<Template> templatesAbstract) {
        this.templatesAbstract = templatesAbstract;
    }

    /**
     * @return the templatesHeading
     */
    public LinkedList<Template> getTemplatesHeading() {
        return templatesHeading;
    }

    /**
     * @param templatesHeading the templatesHeading to set
     */
    public void setTemplatesHeading(LinkedList<Template> templatesHeading) {
        this.templatesHeading = templatesHeading;
    }

    /**
     * @return the finalDocument
     */
    public String getFinalDocument() {
        return finalDocument;
    }

    /**
     * @param finalDocument the finalDocument to set
     */
    public void setFinalDocument(String finalDocument) {
        this.finalDocument = finalDocument;
    }

    /**
     * @return the finalHtmlDokument
     */
    public String getFinalHtmlDocument() {
        return finalHtmlDocument;
    }

    /**
     * @param finalHtmlDokument the finalHtmlDokument to set
     */
    public void setFinalHtmlDocument(String finalHtmlDokument) {
        this.finalHtmlDocument = finalHtmlDokument;
    }

    /**
     * @return the pictureList
     */
    public LinkedList<String> getPictureList() {
        return pictureList;
    }

    /**
     * @return the userInputFiltered
     */
    public LinkedList<String> getUserInputFiltered() {
        return userInputFiltered;
    }

    /**
     * @param userInputFiltered the userInputFiltered to set
     */
    public void setUserInputFiltered(LinkedList<String> userInputFiltered) {
        this.userInputFiltered = userInputFiltered;
    }

    /**
     * @return the templateFill
     */
    public String[] getTemplateFill() {
        return templateFill;
    }

    /**
     * @param templateFill the templateFill to set
     */
    public void setTemplateFill(String[] templateFill) {
        this.templateFill = templateFill;
    }

    /**
     * @return the chosenPicture
     */
    public String getChosenPicture() {
        return chosenPicture;
    }

    /**
     * @param chosenPicture the chosenPicture to set
     */
    public void setChosenPicture(String chosenPicture) {
        this.chosenPicture = chosenPicture;
    }
    
    /**
     * 
     * @param pictureList  the picture List to set
     */
    public void setPictureList(LinkedList<String> pictureList)
    {
        this.pictureList=pictureList;
    }
}
