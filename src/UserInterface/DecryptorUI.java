package UserInterface;

import Algorithms.DesImplementation;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import static Algorithms.Utils.*;

public class DecryptorUI extends JFrame {

    JTextArea area1 = new JTextArea(6, 20);
    JLabel area1_label = new JLabel("Please enter your text to be decrypted");

    JTextArea area2 = new JTextArea(4, 20);
    JLabel area2_label = new JLabel("Key");
    JTextArea area3 = new JTextArea(6, 20);
    JLabel area3_label = new JLabel("Result");

    JScrollPane pane1 = new JScrollPane(area1,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    JScrollPane pane2 = new JScrollPane(area2,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    JScrollPane pane3 = new JScrollPane(area3,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    JButton jb = new JButton("Decrypt");
    JButton jb1 = new JButton("Choose a txt file");

    //Constructor
    public DecryptorUI(String title) {

        super(title);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = area2.getText();
                String contentDecode = area1.getText();
                if (key.length() > 0 && contentDecode.length() > 0) {
//                    Code_generator des = new Code_generator(key);
                    byte[] dataBytes = Base64.decodeBase64(contentDecode);
                    byte[] decode_text_byte = DesImplementation.decrypt_data(dataBytes, key);
                    String decode_textString;
                    try {
                        decode_textString = new String(decode_text_byte, "utf-8");
                        area3.setText(decode_textString);
                    } catch (UnsupportedEncodingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a key and message to be decoded", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                jfc.setFileFilter(filter);
                jfc.showDialog(new JLabel(), "Select");
                File file = jfc.getSelectedFile();
                String contentDecode = null;
                try {
                    contentDecode = read_file(file.getAbsolutePath());
                    if ((area2.getText()).length() > 0 && file != null) {
                        String key = area2.getText();
//                        Code_generator des = new Code_generator(key);
                        byte[] dataBytes = Base64.decodeBase64(contentDecode);
                        byte[] decode_text_byte = DesImplementation.decrypt_data(dataBytes, key);
                        String decode_textString;
                        decode_textString = new String(decode_text_byte, "utf-8");
                        area3.setText(decode_textString);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter a key and select a txt file", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });

        setLayout(new Layout());

        add(area1_label);
        add(pane1);
        add(area2_label);
        add(pane2);
        add(area3_label);
        add(pane3);
        add(jb, BorderLayout.CENTER);
        add(jb1, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

    }

}