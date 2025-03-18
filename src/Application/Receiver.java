package Application;


import UserInterface.Layout;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;




public class Receiver {
    TextArea encryptionText;
    TextArea dataText;
    TextArea keyText;
    Receiver(){
        encryptionText = new TextArea( 5, 40);
        keyText = new TextArea( 1, 40);
        dataText = new TextArea( 5, 40);
        JFrame frame = new JFrame("Receiver");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(panel);

        placeComponents(panel, encryptionText,keyText,dataText);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel,TextArea encryptionText,TextArea keyText,TextArea messageText) {

        panel.setLayout(new Layout());
        JLabel encryptionLabel = new JLabel("Encryption:");

        panel.add(encryptionLabel);

        encryptionText.setEditable(false);
        panel.add(encryptionText);

        JLabel keyLabel = new JLabel("Key:");
        panel.add(keyLabel);

        keyText.setEditable(false);
        panel.add(keyText);

        JLabel messageLabel = new JLabel("Message:");
        panel.add(messageLabel);

        messageText.setEditable(false);
        panel.add(messageText);

    }
    public void getDecryptedData(TextArea messageText) throws IOException {
        String fileName = "D:\\DES_multifunctional_system\\DES\\Receiver\\senderMessageDecrypted.txt";
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String result =  br.readLine();
        messageText.setText(result);
    }
}


