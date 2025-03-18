package Application;

import Algorithms.DesImplementation;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Random;


public class MessageManager {
    public static String getEncryptedMessage(){

        StringBuffer sb = new StringBuffer();
        String tempStr;
        try {
            String fileName = "D:\\DES_multifunctional_system\\DES\\ThirdParty\\senderMessageEncrypted.txt";
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((tempStr = br.readLine()) != null) {
                sb.append(tempStr);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void storeMessage(String s) throws IOException {
        String str = s;
        String fileName = "D:\\DES_multifunctional_system\\DES\\Sender\\senderTextMessage.txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        bw.write(str);
        bw.flush();
        bw.close();
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void generateNewDESkey() {
        try {
            Random random = new Random();

            // Generating a random number between 6 and 10
            int randomNumber = 6 + random.nextInt(5);
            String key = getRandomString(randomNumber);
            String fileName = "D:\\DES_multifunctional_system\\DES\\ThirdParty\\generatedDESKey.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            bw.write(key);
            bw.flush();

            // 生成密钥
            bw.close();
            System.out.println("Key generated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getKey() {
        StringBuffer sb = new StringBuffer();
        String tempStr = new String();
        try {
            String fileName = "D:\\DES_multifunctional_system\\DES\\ThirdParty\\generatedDESKey.txt";
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((tempStr = br.readLine()) != null) {
                sb.append(tempStr);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void encrypt(String file, String dest) throws Exception {


        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(file));
        OutputStream out = new FileOutputStream(dest);
        FileWriter fw = new FileWriter(dest);
        String tempStr;
        while ((tempStr = br.readLine()) != null) {
            sb.append(tempStr);
        }
        String contentDecode = sb.toString();
        byte[] dataBytes = contentDecode.getBytes(Charset.forName("UTF-8"));
        byte[] encode_text_byte = DesImplementation.encrypt_data(dataBytes, getKey());
        String encode_textString = Base64.encodeBase64String(encode_text_byte);

        fw.write(encode_textString);
        fw.close();
        br.close();
        out.close();
        System.out.println("Encrypted file generated");
    }

    public static void decrypt(String file, String dest) throws Exception {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(file));
        OutputStream out = new FileOutputStream(dest);
        FileWriter fw = new FileWriter(dest);
        String tempStr;
        while ((tempStr = br.readLine()) != null) {
            sb.append(tempStr);
        }
        String contentDecode = sb.toString();
        byte[] dataBytes = Base64.decodeBase64(contentDecode);

        byte[] decode_text_byte = DesImplementation.decrypt_data(dataBytes, getKey());
        String decode_textString = null;
        try {
            decode_textString = new String(decode_text_byte, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        fw.write(decode_textString);
        fw.close();
        br.close();
        out.close();
        System.out.println("Decrypted file generated");
    }


}
