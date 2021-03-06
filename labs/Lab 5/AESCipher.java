/**
 * file: AESCipher.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: lab 5
 * due date: March 7th, 2022
 * version: 1.0
 *
 * This file contains the declaration of the AESCipher class.
 */


/**
 * AESCipher
 *
 * This class contains the actual AES cipher, which is called from
 * "Driver_lab5.java" file. 
 * In here we go through 10 rounds of encryption, where we use our round keys
 * that we created in lab4.
 * Before we start, we start with getting our round keys by calling
 * the "AESRoundKeys" function, and use its first round key (out of 11),
 * and send it through the Add Key section before starting the full 1o rounds.
 * The first 9 rounds consist of: 
 * Nibble Substitution --> Shift Rows --> Mix Columns --> Add Key (repeats).
 * The last round consist of:
 * Nibble Substitution--> Shift Rows --> Add Key --> and done.
 * Add key gets the next round key and XOR with the current 4x4 matrix.
 * Mix Columns uses "Rijndael MixColumns", which uses Modular reduction.
 * At the end, we get a single Hex key, which is of length 16 (32 Characters).
 * We return this as in a String[], which is the only string inside the array.
 */
public class AESCipher {

  // S-box taken from "4.pdf".
  private static final int[][] Sbox = new int[][] {
    {0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B,
    0xFE, 0xD7, 0xAB, 0x76},
    {0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF,
    0x9C, 0xA4, 0x72, 0xC0},
    {0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1,
    0x71, 0xD8, 0x31, 0x15},
    {0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2,
    0xEB, 0x27, 0xB2, 0x75},
    {0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3,
    0x29, 0xE3, 0x2F, 0x84},
    {0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39,
    0x4A, 0x4C, 0x58, 0xCF},
    {0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F,
    0x50, 0x3C, 0x9F, 0xA8},
    {0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21,
    0x10, 0xFF, 0xF3, 0xD2},
    {0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D,
    0x64, 0x5D, 0x19, 0x73},
    {0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14,
    0xDE, 0x5E, 0x0B, 0xDB},
    {0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62,
    0x91, 0x95, 0xE4, 0x79},
    {0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA,
    0x65, 0x7A, 0xAE, 0x08},
    {0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F,
    0x4B, 0xBD, 0x8B, 0x8A},
    {0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9,
    0x86, 0xC1, 0x1D, 0x9E},
    {0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9,
    0xCE, 0x55, 0x28, 0xDF},
    {0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F,
    0xB0, 0x54, 0xBB, 0x16}
  };

  // Rcon taken from "4.pdf".
  private static final int[] Rcon = {
    0x8D, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36, 0x6C,
    0xD8, 0xAB, 0x4D, 0x9A, 0x2F, 0x5E, 0xBC, 0x63, 0xC6, 0x97, 0x35, 0x6A,
    0xD4, 0xB3, 0x7D, 0xFA, 0xEF, 0xC5, 0x91, 0x39, 0x72, 0xE4, 0xD3, 0xBD,
    0x61, 0xC2, 0x9F, 0x25, 0x4A, 0x94, 0x33, 0x66, 0xCC, 0x83, 0x1D, 0x3A,
    0x74, 0xE8, 0xCB, 0x8D, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80,
    0x1B, 0x36, 0x6C, 0xD8, 0xAB, 0x4D, 0x9A, 0x2F, 0x5E, 0xBC, 0x63, 0xC6,
    0x97, 0x35, 0x6A, 0xD4, 0xB3, 0x7D, 0xFA, 0xEF, 0xC5, 0x91, 0x39, 0x72,
    0xE4, 0xD3, 0xBD, 0x61, 0xC2, 0x9F, 0x25, 0x4A, 0x94, 0x33, 0x66, 0xCC,
    0x83, 0x1D, 0x3A, 0x74, 0xE8, 0xCB, 0x8D, 0x01, 0x02, 0x04, 0x08, 0x10,
    0x20, 0x40, 0x80, 0x1B, 0x36, 0x6C, 0xD8, 0xAB, 0x4D, 0x9A, 0x2F, 0x5E,
    0xBC, 0x63, 0xC6, 0x97, 0x35, 0x6A, 0xD4, 0xB3, 0x7D, 0xFA, 0xEF, 0xC5,
    0x91, 0x39, 0x72, 0xE4, 0xD3, 0xBD, 0x61, 0xC2, 0x9F, 0x25, 0x4A, 0x94,
    0x33, 0x66, 0xCC, 0x83, 0x1D, 0x3A, 0x74, 0xE8, 0xCB, 0x8D, 0x01, 0x02,
    0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36, 0x6C, 0xD8, 0xAB, 0x4D,
    0x9A, 0x2F, 0x5E, 0xBC, 0x63, 0xC6, 0x97, 0x35, 0x6A, 0xD4, 0xB3, 0x7D,
    0xFA, 0xEF, 0xC5, 0x91, 0x39, 0x72, 0xE4, 0xD3, 0xBD, 0x61, 0xC2, 0x9F,
    0x25, 0x4A, 0x94, 0x33, 0x66, 0xCC, 0x83, 0x1D, 0x3A, 0x74, 0xE8, 0xCB,
    0x8D, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36, 0x6C,
    0xD8, 0xAB, 0x4D, 0x9A, 0x2F, 0x5E, 0xBC, 0x63, 0xC6, 0x97, 0x35, 0x6A,
    0xD4, 0xB3, 0x7D, 0xFA, 0xEF, 0xC5, 0x91, 0x39, 0x72, 0xE4, 0xD3, 0xBD,
    0x61, 0xC2, 0x9F, 0x25, 0x4A, 0x94, 0x33, 0x66, 0xCC, 0x83, 0x1D, 0x3A, 
    0x74, 0xE8, 0xCB, 0x8D };

  /**
   * AESRoundKeys
   *
   * This function takes through the AES round key creation.
   * This is done by taking in a Hex key given by a user,
   * and 11 round keys are given back.
   * The procedures used in this function is based off of lab 4 PDF,
   * and each step is commented and calls-out the section from the PDF.
   * 
   * @param hexKey String, this is the system key Hex given by the user,
   * and is used to create the 11 round keys. 
   * 
   * @return String[], which holds the 11 round keys based off 'hexKey'.
   */
  public static String[] AESRoundKeys(String hexKey){

    String[][] wMatrix = new String[4][44];
    Integer[] wNew = new Integer[4];
    int startingIndex = 0;
    int endingIndex = 2;    
    int hexHolder = 0;

    // This is where the wTable/Matrix is completed, as it has 44 columns.
    for (int j = 0; j < 44; j++){

      // Part one of the PDF, but technically we are filling the first.
      // 4 columns with the hex of the plaintext given. This is round 0.
      if (j < 4){

        for (int i = 0; i < 4; i++) {

          // Needs to be split into separate hex string ('AB').
          wMatrix[i][j] = hexKey.substring(startingIndex, endingIndex);
          startingIndex = endingIndex;
          endingIndex += 2;
        }
      }

      // These are the rest of the rounds (40).

      // If the colum index j is not a multiple of 4 (3.a), XOR the
      // fourth past and last column: w(j) = w(j ??? 4) ^ w(j ??? 1).
      else if (j % 4 != 0) {

        for (int i = 0; i < 4; i++){

          hexHolder = Integer.parseInt(wMatrix[i][j - 4], 16) ^ 
          Integer.parseInt(wMatrix[i][j - 1], 16);
          wMatrix[i][j] = String.format("%02x", hexHolder).toUpperCase();
        }
      }
      // If the colum index j is a multiple of 4 (3.b), we get a wNew:
      // wNew = (Rcon(i) ^ S(w1,j???1)), S(w2,j???1), S(w3,j???1), S(w0,j???1).
      else if (j % 4 == 0) {

        int sBoxHolder = SBoxGet(wMatrix[1][j - 1]);
        // Technically j/4 is 'i' in the PDF.
        int rConHolder = Rcon[j / 4];

        wNew[0] = rConHolder ^ sBoxHolder;
        wNew[1] = SBoxGet(wMatrix[2][j - 1]);
        wNew[2] = SBoxGet(wMatrix[3][j - 1]);
        wNew[3] = SBoxGet(wMatrix[0][j - 1]);

        for (int i = 0; i < 4; i++){

          // w(j) = w(j ??? 4) ^ wNew. 
          hexHolder = Integer.parseInt(wMatrix[i][j - 4], 16) ^  wNew[i];
          wMatrix[i][j] = String.format("%02x", hexHolder).toUpperCase();
        }
      }
    }

    String singleKey = "";
    String[] answer = new String[11];
    int answerCounter = 0;

    // Every round key is then composed of 4 successive readings of the columns
    // of W. E.g., round zero is composed of w(0), w(1), w(2), and w(3); round
    // one will be composed of w(4), w(5), w(6), and w(7); and so on.
    for (int i = 0; i < 44; i ++) {

      for (int j = 0; j < 4; j++) {

        if (singleKey.length() == 32) {

          answer[answerCounter] = singleKey;
          singleKey = "";
          answerCounter++;
        }
        
        singleKey += wMatrix[j][i];
      }
    }
    // Final round key to be added.
    answer[answerCounter] = singleKey;

    return answer;
  }

  /**
   * SBoxGet
   *
   * This function takes in a type string and retrieves a type 'int'
   * hex from the Sbox. 
   * Each part (2 parts) is used to get the row and column of the Sbox.
   * Ex: 8B the corresponding transformed output byte will be 3D, 
   * because you take 8 as the row and B as the column in the table.
   * 
   * @param stringHex String, which is split into 2 to retrieve out of the Sbox.
   * 
   * @return Integer, which which is a type int form of Hex from the Sbox.
   */
  public static Integer SBoxGet(String stringHex){

    int row = Integer.parseInt(stringHex.substring(0,1), 16);
    int column = Integer.parseInt(stringHex.substring(1,2), 16);

    return Sbox[row][column];
  }

  /**
   * AES
   *
   * This function takes us through the actual implementation of AES.
   * We go through 10 rounds, and use the following methods to do so:
   * Nibble Substitution, Shift Rows, Mix Columns, and Add Key.
   * By doing the 10 rounds, we move from having a plain text Hex, and 
   * a key Kex, to having a single cipher-text in Hex.
   * We do use the 'AESRoundKeys' function once, to get our 11 round keys,
   * then at each round, we retrieve the next round key and use it to run
   * through an XOR and complete the round. 
   * As stated earlier, our last round only uses:
   * Nibble Substitution, Shift Rows, and Add Key.
   * We return the cipher-text in a String[] and is the only String inside.
   * 
   * @param pTextHex String, Hex which is plain text and is used to XOR the
   * keyHex before the actual encryption begins.
   * @param keyHex String, Hex which is our key that is to be encrypted.
   * 
   * @return String[], which contains the cipher-text.
   */
  public static String[] AES(String pTextHex, String keyHex){

    String[][] keyHexMatrix = new String[4][4];
    String[][] sHex = new String[4][4];

    // Sending our key to get 11 round keys based off it.
    String[] hexKeys = AESRoundKeys(keyHex);
    
    int startingIndex = 0;
    int endingIndex = 2; 

    // Get the first round key.
    String singleRoundKey = hexKeys[0];   

    // Convert from String Hex to a 4x4 Hex Matrix.
    for (int i = 0; i < 4; i++){

      for (int j = 0; j < 4; j++){

        keyHexMatrix[j][i] = singleRoundKey.substring(startingIndex, endingIndex);
        sHex[j][i] = pTextHex.substring(startingIndex, endingIndex);
        startingIndex = endingIndex;
        endingIndex += 2;
      }

    }

    // Round 0
    String[][] outStateHex = AESStateXOR(keyHexMatrix, sHex);

    for (int i = 0; i < 10; i++) {

      // Rounds 1 to 9.
      if (i < 9) {

        String[][] nibbleSubstitution = AESNibbleSub(outStateHex);
        String[][] shiftRows = AESShiftRow(nibbleSubstitution);
        String[][] mixColumns = AESMixColumn(shiftRows);

        singleRoundKey = hexKeys[i+1];
        startingIndex = 0;
        endingIndex = 2;  

        // Convert the next round key into a 4x4 matrix.
        for (int a = 0; a < 4; a++){      

          for (int j = 0; j < 4; j++){

            keyHexMatrix[j][a] = singleRoundKey.substring(startingIndex, endingIndex);
            startingIndex = endingIndex;
            endingIndex += 2;
          }
        }

        outStateHex = AESStateXOR(keyHexMatrix, mixColumns);
      }
      // Last round (10).
      else {

        String[][] nibbleSubstitution = AESNibbleSub(outStateHex);
        String[][] shiftRows = AESShiftRow(nibbleSubstitution);

        singleRoundKey = hexKeys[i+1];
        startingIndex = 0;
        endingIndex = 2; 

        // Convert the next round key into a 4x4 matrix.
        for (int a = 0; a < 4; a++){      

          for (int j = 0; j < 4; j++){

            keyHexMatrix[j][a] = singleRoundKey.substring(startingIndex, endingIndex);
            startingIndex = endingIndex;
            endingIndex += 2;
          }
        }

        outStateHex = AESStateXOR(keyHexMatrix, shiftRows);
      }

    }

    String answerString = "";

    for (int i = 0; i < outStateHex.length; i++) {

      for (int j = 0; j < outStateHex[0].length; j++) {

        answerString += outStateHex[j][i];
      }
    }

    // As we are only returning the cipher-text in the String array, no need 
    // for a brand new array. 
    return new String[] {answerString};

  }

  /**
   * AESStateXOR
   *
   * This function takes in two 4x4 Hex Matrices and XORs them together.
   * We get another 4x4 matrix once this is done.
   * It is also a main part of the Add Key Operation.
   * 
   * @param sHex String[][], which is one of 2 4x4 matrices. 
   * @param keyHex String[][], which is the second 4x4 matrix. 
   * 
   * @return String[][], which contains the XOR'ed matrix done between
   * the sHex and KeyHex matrices.
   */
  public static String[][] AESStateXOR(String[][] sHex, String[][] keyHex){

    String[][] combinedMatrix = new String[4][4];
    int hex1 = 0;
    int hex2 = 0;
    int hexHolder = 0;

    for (int i = 0; i < 4; i++) {

      for (int j = 0; j < 4; j++) {

        hex1 = Integer.parseInt(sHex[i][j], 16);
        hex2 = Integer.parseInt(keyHex[i][j], 16);
        hexHolder = hex1 ^ hex2;
        
        combinedMatrix[i][j] = String.format("%02x", hexHolder).toUpperCase();
      }
    } 

    return combinedMatrix;
  }

  /**
   * AESNibbleSub
   *
   * This function takes us through a substitution operation, where
   * entries of the output matrix result from running the corresponding 
   * input matrix entries through the AES S-Box.
   * We do so by calling the 'SBoxGet' function created in lab 4 for each
   * Hex pair from inStateHex.
   * We return the same size (4x4 matrix).
   * 
   * @param inStateHex String[][], which is a 4x4 Hex matrix.
   * 
   * @return String[][], which is the substituted 4x4 Hex matrix based off of 
   * the inStateHex 4x4 matrix.
   */
  public static String[][] AESNibbleSub(String[][] inStateHex){

    String[][] substitutedMatrix = new String[4][4];

    for (int i = 0; i < 4; i++) {

      for (int j = 0; j < 4; j++) {

        Integer hexHolder = SBoxGet(inStateHex[i][j]);
        substitutedMatrix[i][j] = String.format("%02x", hexHolder).toUpperCase();
      }
    }  
    

    return substitutedMatrix;
  }

  /**
   * AESShiftRow
   *
   * This function takes us through shifting of a 4x4 Hex matrix.
   * Each level/row is shifted by a different number:
   * Row 1 (or 0): no shift
   * Row 2 (or 1): shift by 1 
   * Row 3 (or 2): shift by 2
   * Row 4 (or 3): shift by 3
   * It then returns a 4x4 Hex matrix based off of the shifts.
   * 
   * @param inStateHex String[][], which is a 4x4 Hex matrix.
   * 
   * @return String[][], which is the shifted 4x4 Hex matrix based off of 
   * the inStateHex 4x4 matrix.
   */
  public static String[][] AESShiftRow(String[][] inStateHex){

    String[][] shiftedMatrix = new String[4][4];

    // Row 1 (index 0) shift, where nothing is shifted.
    for (int i = 0; i < 4; i++) {

      shiftedMatrix[0][i] = inStateHex[0][i];
    }

    // Row 2 (index 1) shift, where shift is by 1.
    for (int i = 0; i < 4; i++) {

      if (i == 3) {

        shiftedMatrix[1][i] = inStateHex[1][0];
      }
      else {

        shiftedMatrix[1][i] = inStateHex[1][i + 1];
      }
    }

    // Row 3 (index 2) shift, where shift is by 2.
    shiftedMatrix[2][0] = inStateHex[2][2];
    shiftedMatrix[2][1] = inStateHex[2][3];
    shiftedMatrix[2][2] = inStateHex[2][0];
    shiftedMatrix[2][3] = inStateHex[2][1];

    // Row 4 (index 3) shift, where shift is by 3.
    shiftedMatrix[3][0] = inStateHex[3][3];
    shiftedMatrix[3][1] = inStateHex[3][0];
    shiftedMatrix[3][2] = inStateHex[3][1];
    shiftedMatrix[3][3] = inStateHex[3][2];

    return shiftedMatrix;
  }

  /**
   * AESMixColumn
   *
   * This function takes us through the mix column operation.
   * Mix Column operation of AES to transform the input state into 
   * output state.
   * This is based off of "https://en.wikipedia.org/wiki/Rijndael_MixColumns".
   * Here is the main algorithm used:
   * s! 0,j = (2*s0,j) ^ (3*s1,j) ^ s2,j ^ s3,j
   * s! 1,j = s0,j ^ (2*s1,j) ^ (3*s2,j) ^ s3,j
   * s! 2,j = s0,j ^ s1,j ^ (2*s2,j) ^ (3*s3,j)
   * s! 3,j = (3*s0,j) ^ s1,j ^ s2,j ^ (2*s3,j)
   * We can see that there is 4 different levels to it, 
   * and each column is accessed.
   * It then returns a 4x4 Hex matrix based off of the mix.
   * 
   * @param inStateHex String[][], which is a 4x4 Hex matrix.
   * 
   * @return String[][], which is the mixed 4x4 Hex matrix based off of 
   * the inStateHex 4x4 matrix.
   */
  public static String[][] AESMixColumn(String[][] inStateHex){

    String[][] mixedMatrix = new String[4][4];
    Integer hexInt1 = 0;
    Integer hexInt2 = 0;
    Integer hexInt3 = 0;
    Integer hexInt4 = 0;
    byte hexByte1 = 0;
    byte hexByte2 = 0;
    byte hexByte3 = 0;
    byte hexByte4 = 0;

    for (int i = 0; i < 4; i++) {

      for (int j = 0; j < 4; j++) {

        // First convert from String to Integer for Hex.
        hexInt1 = Integer.parseInt(inStateHex[0][j], 16);
        hexInt2 = Integer.parseInt(inStateHex[1][j], 16);
        hexInt3 = Integer.parseInt(inStateHex[2][j], 16);
        hexInt4 = Integer.parseInt(inStateHex[3][j], 16);
        
        // Now convert from Integer to Byte for Hex.
        hexByte1 = hexInt1.byteValue();
        hexByte2 = hexInt2.byteValue();
        hexByte3 = hexInt3.byteValue();
        hexByte4 = hexInt4.byteValue();

        if (i == 0) {

          byte singleByteHex = (byte)(GMul((byte)0x02, hexByte1) ^ GMul((byte)0x03, hexByte2) ^ hexByte3 ^ hexByte4);
          mixedMatrix[i][j] = String.format("%02x", singleByteHex).toUpperCase();
        }
        else if (i == 1) {

          byte singleByteHex = (byte)(hexByte1 ^ GMul((byte)0x02, hexByte2) ^ GMul((byte)0x03, hexByte3) ^ hexByte4);
          mixedMatrix[i][j] = String.format("%02x", singleByteHex).toUpperCase();
        }
        else if (i == 2) {

          byte singleByteHex = (byte)(hexByte1 ^ hexByte2 ^ GMul((byte)0x02, hexByte3) ^ GMul((byte)0x03, hexByte4));
          mixedMatrix[i][j] = String.format("%02x", singleByteHex).toUpperCase();
        }
        else if (i == 3) {

          byte singleByteHex = (byte)(GMul((byte)0x03, hexByte1) ^ hexByte2 ^ hexByte3 ^ GMul((byte)0x02, hexByte4));
          mixedMatrix[i][j] = String.format("%02x", singleByteHex).toUpperCase();
        }

      }
    }
    
    return mixedMatrix;
  }

  /**
   * GMul
   *
   * This function has been taken from:
   * "https://en.wikipedia.org/wiki/Rijndael_MixColumns".
   * We can see it walks through the Rijndael cipher, which is the main source
   * of the diffusion in Rijndael.
   * This code was adapted from the 'C' coding language.
   * This follows an 8-bit multiplication with Galois Field.
   * As we converted the Hex strings to bytes, we dont need to worry
   * about going over 8 bits.
   * It then returns a single byte.
   * 
   * @param byte1 byte, the first byte of 2.
   * @param byte2 byte, the second byte of 2.
   * 
   * @return byte, which is the byte that went through the 'special' 
   * multiplication of 2 bytes.
   */
  public static byte GMul(byte byte1, byte byte2) {

    byte answer = 0;
    boolean overBit = true;

    for (int counter = 0; counter < 8; counter++) {

        if ((byte2 & 1) != 0) {

          answer ^= byte1;
        }

        if ((byte1 & 0x80) != 0) {

          overBit = true;
        }
        else {

          overBit = false;
        }

        byte1 <<= 1;

        if (overBit) {

          /* x^8 + x^4 + x^3 + x + 1 */
          byte1 ^= 0x1B; 
        }

        byte2 >>= 1;
    }

    return answer;
  }

}
