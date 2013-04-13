/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author Dawid Rusin, Katharina Sandrock
 */
public class Gui extends JFrame {

    private JLabel backgroundPicture = new JLabel(new ImageIcon("src\\prgenerator\\GUI_backgroundpicture.png"));
    private JTextField userInput = new JTextField();
    private JButton generateTextButton = new JButton();
    private   String finalHtmlDocument = PrGenerator.mainDatabase.getFinalHtmlDocument();

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


        generateTextButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setInput(evt);
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

    private void setInput(ActionEvent evt) {

        PrGenerator.mainDatabase.setUserInput(userInput.getText());

    }

    public void showResult() {
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
        
          closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeFrame(evt);
            }

            private void closeFrame(ActionEvent evt) {
                System.exit(42);
            }
        });

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
                System.out.println(path + " kann gespeichert werden.");
                //PrGenerator.mainDatabase.saveFile(finalHtmlDocument,path);
          } else {
                System.out.println(path + " ist der falsche Dateityp.");
            }

            chooser.setVisible(false);
      return true;
        }
        chooser.setVisible(false);
        return false;
    }
}
