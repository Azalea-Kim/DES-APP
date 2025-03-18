package Algorithms;

import static Algorithms.Utils.*;
import static Algorithms.DesAlgorithms.*;

public class DesImplementation {

    public static byte[] encrypt_data(byte[] inputData, String key) {
        /*
         * input: plain text and key
         * output: text after encryption
         * */

        //make the length of texts fit the algorithm requirement
        byte[] filled_key = Utils.fill_byte_data(key.getBytes());
        byte[] filled_data = Utils.fill_byte_data(inputData);


        int text_len = filled_data.length;
        byte[] encrypted_data = new byte[text_len];
        int count = text_len / 8;
        for (int i = 0; i < count; i++) {
            byte[] tem_key = new byte[8];
            byte[] tem_data = new byte[8];
            System.arraycopy(filled_key, 0, tem_key, 0, 8);
            System.arraycopy(filled_data, i * 8, tem_data, 0, 8);

            // encipher data
            byte[] tem_result = process_data(tem_key, tem_data, 1);
            System.arraycopy(tem_result, 0, encrypted_data, i * 8, 8);
        }
        return encrypted_data;
    }


    public static byte[] decrypt_data(byte[] inputData, String key) {
        /*
         * input: data and key
         * output: data after decryption
         * */

        //make the length of texts fit the algorithm requirement

        byte[] filled_key = Utils.fill_byte_data(key.getBytes());
        byte[] filled_data = Utils.fill_byte_data(inputData);
        int data_len = filled_data.length;
        byte[] decryptedData = new byte[data_len];
        int count = data_len / 8;

        for (int i = 0; i < count; i++) {
            byte[] tem_key = new byte[8];
            byte[] tem_data = new byte[8];
            System.arraycopy(filled_key, 0, tem_key, 0, 8);
            System.arraycopy(filled_data, i * 8, tem_data, 0, 8);

            // decipher data
            byte[] tem_result = process_data(tem_key, tem_data, 0);
            System.arraycopy(tem_result, 0, decryptedData, i * 8, 8);
        }

        byte[] decrypted_data = null;

        int delete_len = decryptedData[data_len - 8 - 1];
        if ((delete_len < 1) || (delete_len > 8)) {
            delete_len = 0;
        }
        decrypted_data = new byte[data_len - delete_len - 8];
        boolean del_flag = true;
        for (int k = 0; k < delete_len; k++) {
            if (delete_len != decryptedData[data_len - 8 - (k + 1)])
                del_flag = false;
        }
        if (del_flag) {
            System.arraycopy(decryptedData, 0, decrypted_data, 0, data_len - delete_len - 8);
        }
        return decrypted_data;
    }


    private static byte[] process_data(byte[] des_key, byte[] des_data, int flag) {
        // Process data provided whether encrypt or decrypt data

        int[] key = Utils.byte_to_binary(des_key);
        int[] data = Utils.byte_to_binary(des_data);

        // generate sub key
        int[][] sub_key = generate_sub_key(key);
        int i;

        // store results after IP
        int[] M = new int[64];
        // store results after IP inverse
        int[] MIP_1 = new int[64];

        // Initial Permutation (IP)
        for (i = 0; i < 64; i++) {

            M[i] = data[IP_FORM[i] - 1];
        }

        // judge encode or decode
        //1 encode，0 decode
        if (flag == 1) {
            // 16 rounds of computation
            for (i = 0; i < 16; i++) {
                f_function(M, i, flag, sub_key);
            }
        } else if (flag == 0) {
            // 16 rounds of computation
            for (i = 15; i > -1; i--) {
                f_function(M, i, flag, sub_key);
            }
        }

        // Inverse of Initial Permutation (IP^(-1))
        for (i = 0; i < 64; i++) {

            MIP_1[i] = M[IP1_FORM[i] - 1];
        }

        return Utils.binary_to_byte(MIP_1);
    }
}