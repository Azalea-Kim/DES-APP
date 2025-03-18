package UserInterface;

import Algorithms.DesImplementation;
import org.apache.commons.codec.binary.Base64;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import static Algorithms.Utils.read_file;

public class EncryptorUI extends JFrame {

    JTextArea area1 = new JTextArea(6, 20);
    JLabel area1_label = new JLabel("Please enter your text to be encrypted");

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

    JButton jb = new JButton("Encrypt");
    JButton jb1 = new JButton("Choose a txt file");

    //Constructor
    public EncryptorUI(String title) {

        super(title);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String key = area2.getText();
                String content = area1.getText();

                if (!key.isEmpty() && !content.isEmpty()) {
                    byte[] dataBytes = content.getBytes(StandardCharsets.UTF_8);
                    byte[] result = DesImplementation.encrypt_data(dataBytes, key);
                    String atest = Base64.encodeBase64String(result);
                    area3.setText(atest);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a key and message to be encoded", "Error", JOptionPane.ERROR_MESSAGE);
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
                String content = null;
                System.out.println(file.getPath());

                try {
                    content = read_file(file.getAbsolutePath());
                    area1.setText(content);
                    System.out.println("The content in the file:");
                    System.out.println(content);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                String key = area2.getText();
                if (!key.isEmpty()) {
                    assert content != null;
                    byte[] dataBytes = content.getBytes(StandardCharsets.UTF_8);

                    byte[] result = DesImplementation.encrypt_data(dataBytes, key);

                    String atest = Base64.encodeBase64String(result);
                    area3.setText(atest);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a key and select a txt file", "Error", JOptionPane.ERROR_MESSAGE);

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