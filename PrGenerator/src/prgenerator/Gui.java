/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author Katharina Sandrock
 */
public class Gui extends JFrame {

    //set variables needed for the frame that will display the starting page 
    private JLabel backgroundPicture = new JLabel(new ImageIcon("src\\prgenerator\\GUI_backgroundpicture.png"));
    private JTextField userInput = new JTextField();
    private JButton generateTextButton = new JButton();
    private String finalHtmlDocument = PrGenerator.mainDatabase.getFinalHtmlDocument();
    private JProgressBar bar = new JProgressBar();

    /*
     * this constructor will display the main page
     *
     */
    public Gui() {



        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backgroundPicture.setBounds(0, 0, 465, 350);

        userInput.setBounds(206, 79, 144, 26);
        userInput.setText("Mögliche Suchbegriffe hier eingeben...");

        generateTextButton.setBounds(240, 135, 77, 26);
        generateTextButton.setBackground(new Color(181, 57, 24));
        generateTextButton.setText("Start");
        generateTextButton.setForeground(Color.white);


        // implementing of an action listener on the "Start"- Button to react on user input
        generateTextButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setInput(evt);
                openBar(evt);

            }
        });

        add(userInput);
        add(generateTextButton);
        add(backgroundPicture);

        setResizable(false);
        setPreferredSize(new Dimension(465, 380));
        setLocation(50, 50);
        pack();
        setVisible(true);

    }

    /*
     * method reacts on the click of the button with filling in the user input
     * into the database and start the modfying method of the input
     */
    private void setInput(ActionEvent evt) {

        PrGenerator.mainDatabase.setUserInput(userInput.getText());
        PrGenerator.mainInputAnalyzer.modifyInputtoString();

    }

    /*
     * this method creates the output frame with the preview of the press
     * release and the possibility to save it as .html document
     */
    public void showResult() {

        bar.setVisible(false);
        JFrame outputFrame = new JFrame();
        JPanel rightPanel = new JPanel();
        JEditorPane leftPanel = new JEditorPane();
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        JButton saveButton = new JButton();
        JButton closeButton = new JButton();
        JButton showInBrowser = new JButton();


        outputFrame.setLayout(null);
        outputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        rightPanel.setBounds(350, 0, 150, 300);
        rightPanel.setBackground(Color.white);

        leftPanel.setEditable(false);
        HTMLEditorKit eKit = new HTMLEditorKit();
        leftPanel.setEditorKit(eKit);
        leftPanel.setText(finalHtmlDocument);

        rightPanel.setLayout(null);
        saveButton.setBounds(20, 50, 100, 26);
        saveButton.setForeground(Color.white);
        saveButton.setBackground(Color.red);
        saveButton.setText("speichern");

        closeButton.setBounds(20, 100, 100, 26);
        closeButton.setForeground(Color.white);
        closeButton.setBackground(Color.red);
        closeButton.setText("beenden");

        rightPanel.add(saveButton);
        rightPanel.add(closeButton);

        outputFrame.add(rightPanel);
        outputFrame.add(leftScrollPane);

        //action listener that reacts on the click of the close Button with the exit of the programm 

        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeFrame(evt);
            }

            private void closeFrame(ActionEvent evt) {
                System.exit(42);
            }
        });


        //save button action listener - calls method saveResult() if save Button was clicked

        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveResult(null);
            }
        });

        outputFrame.setResizable(false);
        outputFrame.setPreferredSize(new Dimension(500, 330));
        outputFrame.setLocation(25, 25);
        outputFrame.pack();
        outputFrame.setVisible(true);
    }

    /**
     * method opens a browser dialog window and implements the possibility to
     * save the file
     *
     */
    public boolean saveResult(String path) {

        JFileChooser chooser;
        if (path == null) {
            path = System.getProperty("user.home");
        }
        File file = new File(path.trim());
        chooser = new JFileChooser(path);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        FileNameExtensionFilter markUpFilter = new FileNameExtensionFilter(
                "Markup: htm, html", "html", "htm");
        chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
        chooser.setFileFilter(markUpFilter);
        chooser.setDialogTitle("Speichern unter...");
        chooser.setVisible(true);

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            path = chooser.getSelectedFile().toString();
            file = new File(path);
            if (markUpFilter.accept(file)) {
                if (!(path.endsWith(".html") || path.endsWith(".htm"))) {
                    path = path + ".html";
                }
                try {
                    PrGenerator.mainDatabase.writeFile(finalHtmlDocument, path);
                } catch (Exception ex) {
                    Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Datei kann unter diesem Dateityp nicht gespeichert werden! Bitte anderen Dateityp angeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }

            chooser.setVisible(false);
            return true;
        }
        chooser.setVisible(false);
        return false;
    }

    private void openBar(ActionEvent evt) {

        bar.setIndeterminate(true);
        bar.setBounds(227, 170, 100, 20);
        add(bar);


    }
}
