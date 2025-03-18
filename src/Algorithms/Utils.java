package Algorithms;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// Functions and constants used for the algorithm
public class Utils {
    // return content inside the file
    public static String read_file(String path) throws IOException {
        StringBuilder file = new StringBuilder();

        FileInputStream fis = new FileInputStream(path);

        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            file.append(line);
        }
        br.close();
        isr.close();
        fis.close();
        return file.toString();
    }


    public static byte[] binary_to_byte(int[] binary_data) {
        //  transform binary to bytes
        byte[] value = new byte[8];
        int i, j;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                value[i] += (binary_data[(i << 3) + j] << (7 - j));
            }
        }
        for (i = 0; i < 8; i++) {
            value[i] %= 256;
            if (value[i] > 128) {
                value[i] -= 255;
            }
        }
        return value;
    }

    static int[] byte_to_binary(byte[] byte_data) {
//        transform byte to binary
        int i;
        int j;
        int[] IntDa = new int[8];
        for (i = 0; i < 8; i++) {
            IntDa[i] = byte_data[i];
            if (IntDa[i] < 0) {
                IntDa[i] += 256;
                IntDa[i] %= 256;
            }
        }
        int[] IntVa = new int[64];
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                IntVa[((i * 8) + 7) - j] = IntDa[i] % 2;
                IntDa[i] = IntDa[i] / 2;
            }
        }
        return IntVa;
    }


    //make the length of texts fit requirement, f
    public static byte[] fill_byte_data(byte[] data) {
        int len = data.length;
        int require = 8 - (len % 8);
        byte[] fillData = new byte[len + require];
        System.arraycopy(data, 0, fillData, 0, len);
        for (int i = len; i < len + require; i++)
            fillData[i] = (byte) require;
        return fillData;
    }

    private static void move_left(int[] array, int offset) {
//        base on the left move form,transform the code
        //Split into two parts, each 28 bits, and perform circular left shifts

        int[] tem_array1 = new int[28];
        int[] tem_array2 = new int[28];
        int[] new_array1 = new int[28];
        int[] new_array2 = new int[28];

        System.arraycopy(array, 0, tem_array1, 0, 28);
        System.arraycopy(array, 28, tem_array2, 0, 28);


        if (offset == 1) {
            System.arraycopy(tem_array1, 1, new_array1, 0, 27);
            System.arraycopy(tem_array2, 1, new_array2, 0, 27);

            new_array1[27] = tem_array1[0];
            new_array2[27] = tem_array2[0];
        } else if (offset == 2) {

            System.arraycopy(tem_array1, 2, new_array1, 0, 26);
            System.arraycopy(tem_array2, 2, new_array2, 0, 26);

            new_array1[26] = tem_array1[0];
            new_array2[26] = tem_array2[0];
            new_array1[27] = tem_array1[1];
            new_array2[27] = tem_array2[1];
        }


        System.arraycopy(new_array1, 0, array, 0, 28);
        System.arraycopy(new_array2, 0, array, 28, 28);

    }

    protected static int[][] generate_sub_key(int[] key) {
//        generate sub key of the given key
        int[][] sub_key = new int[16][48];
        int i, j;
        int[] K0 = new int[56];

        // Perform key compression, compressing the 64-bit key into 56 bits.
        for (i = 0; i < 56; i++) {
            K0[i] = key[PC1_FORM[i] - 1];
        }

        // generate key with 16 iterations
        for (i = 0; i < 16; i++) {
            move_left(K0, LEFT_MOVE_FORM[i]);
            // Perform key compression permutation on the 56-bit key, compressing it into 48 bits
            for (j = 0; j < 48; j++) {
                sub_key[i][j] = K0[PC2_FORM[j] - 1];
            }
        }
        return sub_key;
    }

    // DES Constants
    protected static final int[] IP_FORM = {58, 50, 42, 34, 26, 18, 10, 2, 60, 52,
            44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48,
            40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35,
            27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31,
            23, 15, 7};
    protected static final int[] IP1_FORM = {40, 8, 48, 16, 56, 24, 64, 32, 39, 7,
            47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45,
            13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11,
            51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49,
            17, 57, 25};
    protected static final int[] PC1_FORM = {57, 49, 41, 33, 25, 17, 9, 1, 58, 50,
            42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44,
            36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6,
            61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};
    protected static final int[] LEFT_MOVE_FORM = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    protected static final int[] PC2_FORM = {14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21,
            10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47,
            55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36,
            29, 32}; // 48
    protected static final int[] E_FORM = {32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9,
            10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20,
            21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};
    protected static final int[] P_FORM = {16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23,
            26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22,
            11, 4, 25}; // 32
    protected static final int[][][] SELECT_FORM = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}},
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}};
}
