package Algorithms;

import static Algorithms.Utils.*;

public class DesAlgorithms {
    //  Expansion (E)
    protected static int[] E_algorithm(int[] R0, int[][] key_array, int iteration) {
        // expand 32 bits to 48 bits
        int[] RE = new int[48];
        for (int i = 0; i < 48; i++) {
            RE[i] = R0[E_FORM[i] - 1];
            // Key Mixing, XOR operation between the expanded data with the generated sub key
            RE[i] = RE[i] + key_array[iteration][i];
            if (RE[i] == 2 || RE[i] == 0) {
                RE[i] = 0;
            }
        }
        return RE;
    }

    // S-Box substitution (S)
    protected static int[] S_algorithm(int[] input) {
        int[][] S = new int[8][6];
        int[] selected_box_data = new int[8];
        int[] output = new int[32];

        // 48bits -32 bits
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                S[i][j] = input[(i * 6) + j];
            }
            selected_box_data[i] = SELECT_FORM[i][(S[i][0] << 1) + S[i][5]][(S[i][1] << 3) + (S[i][2] << 2) + (S[i][3] << 1) + S[i][4]];
            for (int j = 0; j < 4; j++) {
                output[((i * 4) + 3) - j] = selected_box_data[i] % 2;
                selected_box_data[i] = selected_box_data[i] / 2;
            }
        }
        return output;
    }

    // Permutation (P)
    protected static void P_algorithm(int[] s_result, int[][] arrays_collection, int flag, int iteration) {
        int[] RP = new int[32];

        int[] M = arrays_collection[0];
        int[] L0 = arrays_collection[1];
        int[] L1 = arrays_collection[2];
        int[] R0 = arrays_collection[3];
        int[] R1 = arrays_collection[4];

        for (int i = 0; i < 32; i++) {
            RP[i] = s_result[P_FORM[i] - 1];
            L1[i] = R0[i];

            // XOR with data from the Left part
            R1[i] = L0[i] + RP[i];
            if (R1[i] == 2 || R1[i] == 0) {
                R1[i] = 0;
            }

            // Swap left and right part
            if (((flag == 0) && (iteration == 0)) || ((flag == 1) && (iteration == 15))) {
                M[i] = R1[i];
                M[i + 32] = L1[i];
            } else {
                M[i] = L1[i];
                M[i + 32] = R1[i];
            }
        }
    }

    // cipher function f
    protected static void f_function(int[] M, int iteration, int flag, int[][] key_array) {
        int[] L0 = new int[32];
        int[] R0 = new int[32];
        int[] L1 = new int[32];
        int[] R1 = new int[32];

        // Split the 64-bit data into left and right blocks
        for (int i = 0; i < 32; i++) {
            L0[i] = M[i];
            R0[i] = M[i + 32];
        }

        int[] e_result = E_algorithm(R0, key_array, iteration);

        int[] s_result = S_algorithm(e_result);

        int[][] arrays = new int[5][];


        arrays[0] = M;
        arrays[1] = L0;
        arrays[2] = L1;
        arrays[3] = R0;
        arrays[4] = R1;


        P_algorithm(s_result, arrays, flag, iteration);
    }
}
