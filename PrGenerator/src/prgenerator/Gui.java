/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prgenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 *
 * @author Dawid Rusin, Katharina Sandrock
 */
public class Gui extends JFrame {
    
        private JLabel backgroundPicture = new JLabel(new ImageIcon("C:/Users/sandrock/DHBW/4. Semester/Software Eng/GUI/GUI_backgroundpicture.png"));
        private JTextField userInput = new JTextField(); 
        private JButton generateTextButton = new JButton();
    
        public Gui(){
        
        setLayout(null);
        setPreferredSize(new Dimension(465, 380));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        backgroundPicture.setBounds(0, 0, 465, 350);
        
        userInput.setBounds(206,79,144,26);
                
        generateTextButton.setBounds(240,135,77,26);
        generateTextButton.setBackground(new Color(181,57,24));
        generateTextButton.setText("Start");
        generateTextButton.setForeground(Color.white);
        add(generateTextButton);
       
        
        generateTextButton.addActionListener(new java.awt.event.ActionListener() {			
        public void actionPerformed(java.awt.event.ActionEvent evt) {				
                setInput(evt);			
            }

        private void setInput(ActionEvent evt) {
                
      
                /* 
                 * 
                 * Database data = new Database();
                 * data.setUserInput(userInput.getText()); 
                 * 
                 */
                 
            }
        });
        
           
        add(backgroundPicture);
        add(userInput);
        add(generateTextButton); 
        
        setLocation(50, 50);		
       	setLocationRelativeTo(null);
        pack();
        setResizable(false);

        setVisible(true);
    }
    
    public void showResult() {
        
    }
    
    public boolean saveResult() {
        return true;
    }
}
