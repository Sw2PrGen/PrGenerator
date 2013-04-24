package prgenerator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * Contains and manages all the data relevant during runtime
 *
 * @author Dawid Rusin
 */
public class Database {

    /*
     * ############################################################
     * ######################## Constants #########################
     * ############################################################
     */
    private final String DHBW_URL = "http://www.dhbw-mannheim.de/";
    private final String DHBW_AKTUELLES_URL = "http://www.dhbw-mannheim.de/aktuelles/details/id/";
    private final String DHBW_MORE_TAG = "<span class=\"more\">";
    private final String DHBW_NEWURL_START = "details/id/";
    private final String DHBW_NEWURL_END = "/\" title";
    private final String BACKUP_FILE_PATH = "data//backup.dat";
    private final String BACKUP_PICTURE_FILE_PATH = "data//picbackup.dat";
    public final String SEARCH_DEFAULT = "Suche...";
    private final String DHBW_PICTURE_TAG = "<div class=\"news-single-img\"><img src=\"";
    private final String BACKUP_PICTURE = "data/backuppic";

    /*
     * ############################################################
     * ######################## Class Vars ########################
     * ############################################################
     */
    private int latestUrlNo;            //url no of the latest news item on the dhbw-site
    private int lastLatestUrlNo = 0;    //ur ni of the latest news item in the backup
    private LinkedList<String> currentData = new LinkedList<>();
    private String userInput = null;
    private String createdText;
    private String createdAbstract;
    private String createdHeading;
    private LinkedList<Template> templatesAbstract = new LinkedList<>();
    private LinkedList<Template> templatesHeading = new LinkedList<>();
    private String finalDocument;
    private String finalHtmlDocument = null;
    private LinkedList<String> pictureList = new LinkedList<>();
    private LinkedList<String> userInputFiltered = new LinkedList<>();
    private String[] templateFill = new String[3];
    private String chosenPicture;
    private String latestUrl;

    /*
     * ############################################################
     * ######################## Logic #############################
     * ############################################################
     */
    /**
     * Analyzes the input string and makes a LinkedList with sentences out of it
     *
     * @param input string to analyze
     * @return list with sentences
     */
    private LinkedList<String> makelist(String input) {
        LinkedList<String> help = new LinkedList<>(Arrays.asList(input.split("[.]")));  //split on "." to get sentences assuming all non-full stops have been masked as # before
        LinkedList<Integer> del = new LinkedList<>();       //list with ids marked for deletion
        for (int i = 0; i < help.size(); i++) {
            help.set(i, help.get(i).trim());
            if (help.get(i).contains("(at)") | help.get(i).length() < 6 | help.get(i).contains("#i#") | help.get(i).contains("#/i#")) {
                del.add(i);     //if a sentence contains suspisious substrings that may render it unusable mark it for deletion
            } else {
                help.set(i, help.get(i).replaceAll("#", "\\.") + ".");  //replace # with . again
            }
        }
        for (int i = 0; i < del.size(); i++) {
            help.remove(del.get(i) - i);    //remove sentences marked for deletion
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
    public LinkedList<String> readFile(String filename) throws Exception {

        FileInputStream fis = new FileInputStream(filename);
        InputStreamReader isr = new InputStreamReader(fis, "UTF8");
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = isr.read()) != -1) {
            buffer.append((char) c);
        }
        String str = buffer.toString();
        String[] lines = str.split("\n");
        fis.close();
        isr.close();
        return new LinkedList<String>(Arrays.asList(lines));
    }

    /**
     * Writes a file on the hdd
     *
     * @param text the content of the file to write
     * @param filename the name of the file
     * @throws Exception
     */
    public void writeFile(String text, String filename) throws Exception {
        String[] str = filename.split("/");
        String dirs = new String();
        for (int i = 0; i < str.length - 1; i++) {
            dirs += str[i] + "/";
        }
        File file = new File(dirs);
        file.mkdirs();
        BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
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
        while (i >= lastLatestUrlNo) {    //stop if the news item is in the backup to crank up performance
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
            return false;
        }
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
            if (latestUrlNo <= lastLatestUrlNo) {   //if the latest news is in the backup return null to indicate that
                return null;
            }
            reader.close();
            return DHBW_AKTUELLES_URL + s;
        } catch (Exception ex) {
            try {
                reader.close();
            } catch (Exception e) {
            }
            return null;
        }
    }

    /**
     * Sarches for a regular expression in a string and replaces a substring of
     * the regular expression with a replacement string
     *
     * @param string input string to search for the regex
     * @param regex the regular expression
     * @param lookFor substring to be replaced, can be a regex
     * @param replaceWith replacement
     * @return the strin gwith replaced substrings
     */
    private String replaceSpecPattern(String string, String regex, String lookFor, String replaceWith) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        String help;
        while (matcher.find()) {
            help = matcher.group(matcher.groupCount());
            string = string.replace(help, help.replaceAll(lookFor, replaceWith));
        }
        return string;
    }

    /**
     * Searches an online press release for the picture
     * @param reader the reader with the web site, will not be closed!
     * @return the URL to the picture
     */
    private String getPicture(BufferedReader reader) {
        String s = null;
        do {  //go to the text passage in the html-code
            try {
                s = reader.readLine();
            } catch (Exception e) {
            }
        } while (s != null && !s.contains(DHBW_PICTURE_TAG));
        if (s == null || !s.contains(".jpg")) {
            return null;
        }
        try {
            s = DHBW_URL + s.substring(s.indexOf(DHBW_PICTURE_TAG) + DHBW_PICTURE_TAG.length() + 1, s.indexOf(".jpg")) + ".jpg";
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * Downloads the picture specified and saves it as BACKUP_PICTURE + num + .jpg
     * @param link URL to the picture
     * @param num index for the picture name
     * @return true if successful, false otherwise
     */
    private boolean downloadPicture(String link, int num) {
        try {
            URL url = new URL(link);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(BACKUP_PICTURE + Integer.toString(num) + ".jpg");
            fos.write(response);
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
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
                return null;
            }
        }
        try {
            while ((s = reader.readLine()) != null && !s.contains("</div>")) {  //read all until the end of the text passage
                if (s.contains("www") | s.contains("<a>") | s.contains("<i>") | s.contains("<td ")) {
                    continue;
                }
                s = s.replaceAll("\t", "");                                     //delete all tabulators
                s = s.replaceAll("<(\"[^\"]*\"|'[^']*'|[^'\">])*>", "");        //delete all html-tags
                s = s.replaceAll("&nbsp;", "");                                 //delete "&nbsp;"
                s = s.replaceAll(".+[^(. )]$", "");                             //delete every codeline that does not conclude with a full stop
                s = s.replaceAll("„", "");                                      //delete lower "
                s = s.replaceAll("“", "");                                      //delete upper "
                s = s.replaceAll("&amp;", "&");                                 //replace amp with &
                s = s.replaceAll("&quot;", "");                                 //delete &quot;

                s = replaceSpecPattern(s, "\\d+\\.", "\\.", "#");               //replace all . in dates
                s = replaceSpecPattern(s, " [a-zA-Z]{1,3}\\.", "\\.", "#");     //replace all . in abreviations
                s = replaceSpecPattern(s, "#[a-zA-Z]{1,3}\\.", "\\.", "#");     //replace all . in janky abbreviations
                s = replaceSpecPattern(s, "Prof\\.", "\\.", "#");               //mark some abbreviations longer than 3 letters
                s = replaceSpecPattern(s, "Dipl\\.", "\\.", "#");
                s = replaceSpecPattern(s, "Angl\\.", "\\.", "#");
                s = replaceSpecPattern(s, "bspw\\.", "\\.", "#");
                s = replaceSpecPattern(s, "evtl\\.", "\\.", "#");

                s = s.trim();
                sb.append(s);                                                   //append to the stringbuilder
            }
        } catch (Exception e) {
            return null;
        }

        finishedText = sb.toString();                                           //make a string out of the stringbuilder
        try {
            reader.close();
        } catch (IOException ex) {
        }
        return finishedText;
    }

    /**
     * tries to load new data from the dhbw-site
     *
     * @return true if new data was loaded, false otherwise
     */
    private boolean loadNewData() {
        LinkedList<String> backupFile = null;
        try {
            backupFile = readFile(BACKUP_FILE_PATH);    //open the backup file
            lastLatestUrlNo = Integer.parseInt(backupFile.pop());   //get the no of the latest news item from the backup
        } catch (Exception e) {
            lastLatestUrlNo = 0;
        }
        //String latestUrl = getLatestNewsUrl();      //get the url to the latest news item
        latestUrl = getLatestNewsUrl();
        if (latestUrl == null) {
            return false;               //no conncetion or latest item in backup
        }

        int i = 0;
        do {          //stop either on 100 news items or when the item id reaches the id in the backup
            BufferedReader r = getWebsite(latestUrl);
            String s = getPicture(r);
            if (s != null) {
                pictureList.add(s);
            }
            if ((s = getText(r)) == null) {
                continue;
            }
            currentData.addAll(makelist(s));   //write the data into the linked list
            i++;
        } while (i < 101 & (latestUrl = getNextUrl(latestUrl)) != null);

        if (backupFile != null) {
            LinkedList<String> picBackupFile = null;
            try {
                picBackupFile = readFile(BACKUP_PICTURE_FILE_PATH);
            } catch (Exception e) {
            }
            String s;// = backupFile.pop();
            String p;
            while (currentData.size() < 1100 && backupFile.size() > 0) {    //fill with backup data
                s = backupFile.pop();
                p = picBackupFile.pop();
                if (p != null) {
                    pictureList.add(p);
                }
                currentData.add(s);
            }
        }
        updateBackup(false);     //update the backup file with the currently loaded data
        updateBackup(true);
        return true;
    }

    /**
     * Loads the backup file into currentData
     *
     * @return true if the file could be loaded, false otherwise
     */
    private boolean loadBackup() {
        LinkedList<String> backupFile = null;
        try {
            backupFile = readFile(BACKUP_FILE_PATH);
            backupFile.pop();
        } catch (Exception e) {
            return false;
        }
        String s;
        do {
            s = backupFile.pop();
            currentData.add(s);
        } while (backupFile.size() > 0);
        if (checkWebsite("http://google.com")) {

            backupFile = null;
            try {
                backupFile = readFile(BACKUP_PICTURE_FILE_PATH);
            } catch (Exception e) {
                return false;
            }
            do {
                s = backupFile.pop();
                pictureList.add(s);
            } while (backupFile.size() > 0);
            return true;
        } else {
            String path = "file://" + PrGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("PrGenerator.jar", "");
            try {
                path = URLDecoder.decode(path, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                JOptionPane.showConfirmDialog(null, path + " kann nicht un UTF-8 kodiert werden, es ist nicht möglich Backupbilder zu laden.", "Encoding Fehler", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            }
            for (int i = 0; i < 5; i++) {
                pictureList.add(path + BACKUP_PICTURE + Integer.toString(i) + ".jpg");
            }
            return true;
        }
    }

    /**
     * save currentData or pictureList as a backup
     * @param pic determines if you want to save the pictureList
     * @return true if successful, false otherwise
     */
    private boolean updateBackup(boolean pic) {
        StringBuilder sb = new StringBuilder();
        LinkedList<String> help = null;
        if(pic){
            help = new LinkedList<>(pictureList);
        } else{
            help = new LinkedList<>(currentData);
        }
        if (!pic) {
            sb.append(Integer.toString(latestUrlNo));
            sb.append("\n");
        } else {
            int i = 0;
            int j = 0;
            do {
                if (downloadPicture(help.get(i), j) != false) {
                    j++;
                }
                i++;
            } while (j < 5 && help.size() > i);
        }
        
        while (help.size() > 0) {
            sb.append(help.pop());
            sb.append("\n");
        }
        String filepath;
            if(pic){
                filepath = BACKUP_PICTURE_FILE_PATH;
            }else {
                filepath = BACKUP_FILE_PATH;
            }
        try {
            writeFile(sb.toString(), filepath);
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, "Backupdatei " + filepath + " kann nicht geupdated werden", "Dateifehler", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Tries to pull new data from the DHBW-Site and update the backup
     * afterwards, loads the backup otherwise
     */
    public void manageData() {
        boolean controlVar;
        controlVar = loadNewData();      //try to get new data
        if (!controlVar) {
            controlVar = loadBackup();      //load backup if no connection or no new items
        }
        if (!controlVar){
            JOptionPane.showConfirmDialog(null, "Kritischer Fehler! \nEs können keine Pressemitteilungen \naus dem Internet geladen werden \nund die Backup-Datei kann nicht geöffnet werden. \nGenerierung der Pressemitteilung ist nicht möglich.\nDas Programm wird beendet.", "Kritischer Fehler", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * ############################################################
     * ################## Getter and setter #######################
     * ############################################################
     */
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
     * @param pictureList the picture List to set
     */
    public void setPictureList(LinkedList<String> pictureList) {
        this.pictureList = new LinkedList<>(pictureList);
    }

    /**
     * 
     * @return the constant SEARCH_DEFAULT
     */
    @Deprecated public String getSEARCH_DEFAULT() {
        return SEARCH_DEFAULT;
    }
}
