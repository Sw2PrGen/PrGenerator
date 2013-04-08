/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

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
    private LinkedList<String> currentData;
    private String userInput;
    private String createdText;
    private String createdAbstract;
    private String createdHeading;
    private LinkedList<Template> templatesAbstract;
    private LinkedList<Template> templatesHeading;
    private String finalDocument;
    private String finalHtmlDocument;
    private LinkedList<String> pictureList;
    private LinkedList<String> userInputFiltered;
    private String[] templateFill;
    private String chosenPicture;

    private String getNextUrl(String url) {
        int i = Integer.parseInt(url.split("/")[url.split("/").length - 1]) -1;
        while (i <= 0) {
            String nexturl = Integer.toString(i);
            BufferedReader reader = getWebsite(nexturl);
            try{
                String s;
                while((s = reader.readLine()) !=null){
                    if(s.contains("<title>Page not found</title>")){
                        
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            
        }
        return "";
    }

    private BufferedReader getWebsite(String adress) {
        BufferedReader reader = null;
        try {
            URL url = new URL(adress);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reader;
    }

    private String getLatestNewsUrl() {
        String s = "";
        BufferedReader reader = getWebsite(DHBW_URL);
        try {
            while (s != null & !s.contains(DHBW_MORE_TAG)) {
                s = reader.readLine();
            }
            if (s == null) {
                return s;
            }
            s = s.substring(s.indexOf(DHBW_NEWURL_START) + DHBW_NEWURL_START.length(), s.indexOf(DHBW_NEWURL_END));
            return DHBW_AKTUELLES_URL + s;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }

    private String getText(BufferedReader reader) {
        String finishedText = "";
        String s = "";
        StringBuilder sb = new StringBuilder();
        while (s != null & !s.contains("<div class=\"csc-textpic-text\">")) {
            try {
                s = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            while ((s = reader.readLine()) != null && !s.contains("</div>")) {
                s = s.replaceAll("\t", "");
                s = s.replaceAll("<(\"[^\"]*\"|'[^']*'|[^'\">])*>", "");
                sb.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        finishedText = sb.toString();
        return finishedText;
    }

    private boolean loadNewData() {
        String latestUrl = getLatestNewsUrl();
        if (latestUrl == null) {
            return false;
        }
        String finishedText = getText(getWebsite(latestUrl));
        System.out.print(finishedText);

        return true;
    }

    private boolean loadBackup() {
        return true;
    }

    private boolean updateBackup() {
        return true;
    }

    private boolean isLoaded() {
        return true;
    }

    public void manageData() {
        loadNewData();
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
}
