/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author Katharina Sandrock
 */
public class Gui extends JFrame implements Runnable {

    //set variables needed for the frame that will display the starting page 
    private String finalHtmlDocument;// = PrGenerator.mainDatabase.getFinalHtmlDocument();
    private JProgressBar bar = new JProgressBar();
    private JTextField userInput = new JTextField();


    /*
     * this constructor will display the main page
     *
     */

    public void initializeGui(){
        JLabel backgroundPicture = new JLabel(new ImageIcon("src\\sources\\GUI_backgroundpicture.png"));
        JButton generateTextButton = new JButton();

        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        openBar();
        bar.setVisible(false);
        
        backgroundPicture.setBounds(0, 0, 465, 350);

        userInput.setBounds(206, 79, 144, 26);
        userInput.setText(PrGenerator.mainDatabase.getSEARCH_DEFAULT());
        userInput.setSelectionStart(0);
        userInput.setSelectionEnd(userInput.getText().length());
        userInput.setToolTipText("max. 50 Zeichen erlaubt");

        generateTextButton.setBounds(240, 135, 77, 26);
        generateTextButton.setBackground(new Color(181, 57, 24));
        generateTextButton.setText("Start");
        generateTextButton.setForeground(Color.white);
        generateTextButton.setToolTipText("erstellt Pressemitteilung");
        

        // implementing of an action listener on the "Start"- Button to react on user input
        generateTextButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                 setInput(evt);                
            }
        });
        
        userInput.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if (userInput.getText().equals(PrGenerator.mainDatabase.getSEARCH_DEFAULT())) {
                                    userInput.setSelectionStart(0);
                userInput.setSelectionEnd(userInput.toString().length()-1);
                }

            }
        });
        
        
                

        add(userInput);
        add(generateTextButton);
        add(backgroundPicture);

        setResizable(false);
        setPreferredSize(new Dimension(465, 380));
        setLocation(getCoords(465,380));
        pack();
        setVisible(true);

    }

    /*
     * method reacts on the click of the button with filling in the user input
     * into the database and start the modfying method of the input
     */
    private void setInput(ActionEvent evt) {

        if (userInput.getText().length() <= 50) {
            bar.setVisible(true);
            PrGenerator.mainDatabase.setUserInput(userInput.getText());
           // PrGenerator.doit();
        } else {
            
            JOptionPane.showMessageDialog(null, "Eingabe zu lang! (max. erlaubte Zeichen: 50) ");
        }
        
        //PrGenerator.mainInputAnalyzer.modifyInputtoString();

    }

    /*
     * this method creates the output frame with the preview of the press
     * release and the possibility to save it as .html document
     */
    public void showResult() {

        finalHtmlDocument = PrGenerator.mainDatabase.getFinalHtmlDocument();
        bar.setVisible(false);
        final JFrame outputFrame = new JFrame();
        JPanel lowerPanel = new JPanel();
        JEditorPane mainPanel = new JEditorPane();
        JScrollPane leftScrollPane = new JScrollPane(mainPanel);
        JButton saveButton = new JButton();
        JButton closeButton = new JButton();
        JButton backButton = new JButton();


        outputFrame.setLayout(null);
        outputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        lowerPanel.setBounds(0, 570, 550, 50);
        lowerPanel.setBackground(Color.white);

        mainPanel.setEditable(false);
        HTMLEditorKit eKit = new HTMLEditorKit();
        mainPanel.setEditorKit(eKit);
        mainPanel.setText(finalHtmlDocument);
        mainPanel.setCaretPosition(0);


        leftScrollPane.setBounds(0, 0, 550, 570);
        leftScrollPane.setBackground(Color.white);
        leftScrollPane.getVerticalScrollBar().setBackground(new Color(202, 202, 205));
       
 
        lowerPanel.setLayout(null);
        
        saveButton.setBounds(110, 13, 100, 26);
        saveButton.setForeground(Color.white);
        saveButton.setBackground(new Color(181, 57, 24));
        saveButton.setText("speichern");
        saveButton.setToolTipText("Text als .html speichern.");
        
        backButton.setBounds(225,13,100,26);
        backButton.setForeground(Color.white);
        backButton.setBackground(new Color(181, 57, 24));
        backButton.setText("zurück");
        backButton.setToolTipText("zurück zum Startfenster");
        
        closeButton.setBounds(340, 13, 100, 26);
        closeButton.setForeground(Color.white);
        closeButton.setBackground(new Color(181, 57, 24));
        closeButton.setText("beenden");
        closeButton.setToolTipText("beenden des Programms");

        lowerPanel.add(saveButton);
        lowerPanel.add(backButton);
        lowerPanel.add(closeButton);

        outputFrame.add(lowerPanel);
        outputFrame.add(leftScrollPane);

        //action listener that reacts on the click of the close Button with the exit of the programm 

        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeFrame(evt);
            }

            private void closeFrame(ActionEvent evt) {
                System.exit(0);
            }
        });

        backButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               outputFrame.dispose();
            }
            });

        //save button action listener - calls method saveResult() if save Button was clicked

        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveResult(null);
            }
        });

        outputFrame.setResizable(false);
        outputFrame.setPreferredSize(new Dimension(555, 650));     
        outputFrame.setLocation(getCoords(555,650));
        outputFrame.pack();
        outputFrame.setVisible(true);
        PrGenerator.mainDatabase.setFinalHtmlDocument(null);
        PrGenerator.mainDatabase.setUserInput(null);
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

            if (!(path.endsWith(".html") || path.endsWith(".htm"))) {
                path = path + ".html";
            }
            try {
                PrGenerator.mainDatabase.writeFile(finalHtmlDocument, path);
            } catch (Exception ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }

            chooser.setVisible(false);
            return true;
        }
        chooser.setVisible(false);
        return false;
    }
    
    private Point getCoords (int x, int y){
        
        int xScreen = Toolkit.getDefaultToolkit().getScreenSize().width;
        int yScreen = Toolkit.getDefaultToolkit().getScreenSize().height;
        return new Point(((xScreen/2) - (x/2)),((yScreen/2)) - (y/2));
        
        
    }

    private void openBar() {

        bar.setIndeterminate(true);
        bar.setBounds(227, 170, 100, 20);
        bar.setForeground(new Color(181, 57, 24));
        bar.setBackground(new Color(202, 202, 205));
        add(bar);


    }

    @Override
    public void run() {
       
        initializeGui();
         while (true) {
        while (PrGenerator.mainDatabase.getFinalHtmlDocument() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                //Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
        showResult();
        
        }
    }
    
}
