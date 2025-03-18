package Application;


import UserInterface.Layout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;


public class Sender {
    static Receiver receiverUI;
    static JFileChooser chooser;
    static String fileName = null;
    static String newFileName = null;
    static String filePath = null;
    Sender() {
        receiverUI = new Receiver();
        JFrame frame = new JFrame("Sender");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.TOP);

        JLabel[] jLabels = new JLabel[2];

        jLabels[0] = new JLabel();
        placeComponents(jLabels[0]);
        jTabbedPane.add("Send a message", jLabels[0]);

        jLabels[1] = new JLabel();
        placeComponents2(jLabels[1]);
        jTabbedPane.add("Send a file", jLabels[1]);

        frame.add(jTabbedPane);

        frame.setVisible(true);
    }

    private static void placeComponents(JLabel panel) {

        panel.setLayout(new Layout());
        JLabel messageLabel = new JLabel("Message:");
        panel.add(messageLabel);
        TextArea messageText = new TextArea(5, 40);
        panel.add(messageText);

        JLabel keyLabel = new JLabel("Key:");
        panel.add(keyLabel);
        TextArea keyText = new TextArea(1, 40);
        keyText.setEditable(false);
        panel.add(keyText);

        JLabel encryptionLabel = new JLabel("Encryption:");
        panel.add(encryptionLabel);
        TextArea encryptionText = new TextArea(5, 40);
        encryptionText.setEditable(false);
        panel.add(encryptionText);

        JButton loginButton = new JButton("Send");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Save the sender message into a file ./Sender/senderTextMessage.txt
                    MessageManager.storeMessage(messageText.getText());

                    // Generate and save a new DES key
                    MessageManager.generateNewDESkey();

                    // Show the generated key on the sender side
                    keyText.setText(MessageManager.getKey());

                    // Encrypt the message with the generated key
                    // save the encryption in ./ThirdParty/senderMessageEncrypted.txt
                    MessageManager.encrypt("D:\\DES_multifunctional_system\\DES\\Sender\\senderTextMessage.txt", "D:\\DES_multifunctional_system\\DES\\ThirdParty\\senderMessageEncrypted.txt");

                    // Read and Show the encrypted message on the sender side
                    encryptionText.setText(MessageManager.getEncryptedMessage());

                    // decrypt senderMessageEncrypted.txt
                    // save the decryption in ./Receiver/senderMessageDecrypted.txt
                    MessageManager.decrypt("D:\\DES_multifunctional_system\\DES\\ThirdParty\\senderMessageEncrypted.txt", "D:\\DES_multifunctional_system\\DES\\Receiver\\senderMessageDecrypted.txt");

                    // read decrypted data and show on the receiver side
                    receiverUI.getDecryptedData(receiverUI.dataText);

                    // Show the generated key on the receiver side
                    receiverUI.keyText.setText(MessageManager.getKey());

                    // Read and Show the encrypted message on the receiver side
                    receiverUI.encryptionText.setText(MessageManager.getEncryptedMessage());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        panel.add(loginButton);
    }

    private static void placeComponents2(JLabel panel) {
        // Initialize file chooser
        chooser = new JFileChooser("D:\\DES_multifunctional_system\\DES\\Sender"); 

        panel.setLayout(new Layout());
        JLabel messageLabel = new JLabel("File Information:");
        panel.add(messageLabel);
        TextArea messageText = new TextArea(15, 40);
        messageText.setEditable(false);
        panel.add(messageText);
        JButton actionButton = new JButton("Select a file");
        actionButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int state; //file chooser state
                chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter()); //remove all file filters
                state = chooser.showOpenDialog(null); //open dialog
                File file = chooser.getSelectedFile(); //get the selected file

                filePath = file.getPath();
                messageText.setText("File Path: "+filePath);
                fileName = file.getName();
                newFileName ="(Encrypted)"+fileName;
                messageText.append("\nFile name: "+file.getName());

            }
        });
        panel.add(actionButton);
        JButton actionButton2 = new JButton("Send");
        actionButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Pop up, selection of a file needed
                if (messageText.getText().length()==0){
                    JOptionPane.showMessageDialog(null, "Please select a file first"); 
                }else{

                    FileManager fileManager = new FileManager();
                    try {
                        // encrypt a new file to the Receiver folder
                        fileManager.encrypt(filePath,"D:\\DES_multifunctional_system\\DES\\Receiver\\"+newFileName);

                        // decrypt the received file to the Receiver folder
                        fileManager.decrypt("D:\\DES_multifunctional_system\\DES\\Receiver\\"+newFileName,"D:\\DES_multifunctional_system\\DES\\Receiver\\"+fileName);
                        messageText.append("\nSend File Succeed");

                        receiverUI.encryptionText.setText("Receive new File");
                        receiverUI.encryptionText.append("\nFile name: "+newFileName);
                        receiverUI.keyText.setText(String.valueOf(fileManager.key));
                        receiverUI.dataText.setText("Decrypted file generated");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        panel.add(actionButton2);


    }
}


