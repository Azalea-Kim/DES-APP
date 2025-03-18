package Application;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;

public class FileManager {
    Key key;
    public FileManager() {
        //generate DES key javax.crypto.spec.SecretKeySpec@18529
        byte[] bys = new byte[] { 7, 16, -15, -3, 4, -42, 14, 64};
        this.key = new SecretKeySpec(bys, "DES");
    }


    // encrypt the file using DES in javax.crypto.Cipher, and store the encrypted file
    public void encrypt(String file, String destFile) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        // cipher.init(Cipher.ENCRYPT_MODE, getKey());
        cipher.init(Cipher.ENCRYPT_MODE, this.key);
        InputStream is = new FileInputStream(file);
        OutputStream out = new FileOutputStream(destFile);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = cis.read(buffer)) > 0) {
            out.write(buffer, 0, r);
        }

        cis.close();
        is.close();
        out.close();
    }


    //decrypt the encrypted file using DES in javax.crypto.Cipher, and store the decrypted file
    public String decrypt(String file, String destFile) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, this.key);

        InputStream is = new FileInputStream(file);
        String PateoMessage = "";
        OutputStream out = new FileOutputStream(destFile);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

    CipherOutputStream cos = new CipherOutputStream(out, cipher);


        byte[] buffer = new byte[1024];
        int r;
        while ((r = is.read(buffer)) >= 0) {
            cos.write(buffer, 0, r);
        }

        cos.close();
        out.close();
        is.close();
        PateoMessage=baos.toString();
        baos.close();
        return PateoMessage;
    }

}

