package prgenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;

/**
 * displays the GUI
 *
 * @author Katharina Sandrock
 */
public class Gui extends JFrame implements Runnable {

    //set variables needed for the frame that will display the starting page 
    private String finalHtmlDocument;
    private JProgressBar bar = new JProgressBar();
    private JTextField userInput = new JTextField();
    private JButton generateTextButton = new JButton();
    private JFrame outputFrame;

    /**
     * this method will display the main page
     */
    public void initializeGui() {

        JLabel backgroundPicture = new JLabel(new ImageIcon(getClass().getResource("/sources/GUI_backgroundpicture.png"))); //changed by Dawid to ensure standalone *.jar functionality


        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("PrGenerator");

        openBar();
        bar.setVisible(false);

        backgroundPicture.setBounds(0, 0, 465, 350);

        userInput.setBounds(206, 79, 144, 26);
        userInput.setText(PrGenerator.mainDatabase.SEARCH_DEFAULT);
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
            public void actionPerformed(ActionEvent evt) {
                setInput(evt);
                userInput.setEnabled(false);
                generateTextButton.setEnabled(false);
            }
        });
        // set the generateTextButton as default button to start the generation of the press release also if the user presses enter
        getRootPane().setDefaultButton(generateTextButton);

        //marks the input text field so the user can easily and fast remove the default input
        userInput.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (userInput.getText().equals(PrGenerator.mainDatabase.SEARCH_DEFAULT)) {
                    userInput.setSelectionStart(0);
                    userInput.setSelectionEnd(userInput.toString().length() - 1);
                }

            }
        });

        add(userInput);
        add(generateTextButton);
        add(backgroundPicture);

        setResizable(false);
        setPreferredSize(new Dimension(465, 380));
        setLocation(getCoords(465, 380));
        pack();
        setVisible(true);

    }

    /**
     * method reacts on the click of the button with filling in the user input
     * into the database and start the modifying method of the input
     */
    private void setInput(ActionEvent evt) {

        if (userInput.getText().length() <= 50) {

            bar.setVisible(true);
            PrGenerator.mainDatabase.setUserInput(userInput.getText());

        } else {

            JOptionPane.showMessageDialog(null, "Eingabe zu lang! (max. erlaubte Zeichen: 50) ");
        }

    }

    /**
     * this method creates the output frame with the preview of the press
     * release and the possibility to save it as HTML document
     */
    public void showResult() {

        finalHtmlDocument = PrGenerator.mainDatabase.getFinalHtmlDocument();
        bar.setVisible(false);
        outputFrame = new JFrame();

        outputFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                resetGui();
            }
        });
        JPanel lowerPanel = new JPanel();
        JEditorPane mainPanel = new JEditorPane();
        JScrollPane leftScrollPane = new JScrollPane(mainPanel);
        JButton saveButton = new JButton();
        JButton closeButton = new JButton();
        JButton backButton = new JButton();


        outputFrame.setLayout(null);
        outputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        outputFrame.setTitle("Vorschau");

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

        backButton.setBounds(225, 13, 100, 26);
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

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeFrame(evt);
            }

            private void closeFrame(ActionEvent evt) {
                System.exit(0);
            }
        });

        backButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetGui();
            }
        });

        //save button action listener - calls method saveResult() if save Button was clicked

        saveButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveResult();
            }
        });

        outputFrame.setResizable(false);
        outputFrame.setPreferredSize(new Dimension(555, 650));
        outputFrame.setLocation(getCoords(555, 650));
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
    public void saveResult() {

        String path = System.getProperty("user.home");
        JFileChooser chooser;
        chooser = new JFileChooser(path);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);

        //allows only html format 
        FileNameExtensionFilter markUpFilter = new FileNameExtensionFilter(
                "Markup: htm, html", "html", "htm");
        chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
        chooser.setFileFilter(markUpFilter);
        chooser.setDialogTitle("Speichern unter...");
        chooser.setVisible(true);

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            path = chooser.getSelectedFile().toString();

            //sets automatically the right ending 
            if (!(path.endsWith(".html") || path.endsWith(".htm"))) {
                path = path + ".html";
            }

            try {
                PrGenerator.mainDatabase.writeFile(finalHtmlDocument, path);
                JOptionPane.showMessageDialog(null, "Datei wurde gespeichert in: \"" + path + "\"");

            } catch (Exception ex) {

                JOptionPane.showConfirmDialog(null, "Datei konnte nicht gespeichert werden. Bitte ungültige Sonderzeichen vermeiden.", "Fehler", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    /**
     * calculates the coordinates to display the GUI in the center of the screen
     *
     * @return coordinates of the upper left corner
     */
    private Point getCoords(int x, int y) {

        int xScreen = Toolkit.getDefaultToolkit().getScreenSize().width;
        int yScreen = Toolkit.getDefaultToolkit().getScreenSize().height;
        return new Point(((xScreen / 2) - (x / 2)), ((yScreen / 2)) - (y / 2));


    }

    /**
     * draws a progress bar onto the GUI
     */
    private void openBar() {

        bar.setIndeterminate(true);
        bar.setBounds(227, 170, 100, 20);
        bar.setForeground(new Color(181, 57, 24));
        bar.setBackground(new Color(202, 202, 205));
        add(bar);


    }

    public void resetGui() {
        PrGenerator.mainDatabase.setPictureList(new LinkedList<String>());
        outputFrame.dispose();
        userInput.setEnabled(true);
        generateTextButton.setEnabled(true);
    }

    /**
     * run method for the GUI Thread
     */
    @Override
    public void run() {

        initializeGui();
        while (true) { //loop enables main function of the GUI
            while (PrGenerator.mainDatabase.getFinalHtmlDocument() == null) { // Gui waits until output press release is generated 
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            showResult(); //after the showing of the result the program can be exited or started again (finalHtmlDocument and userinput are set to null)

        }
    }
}
