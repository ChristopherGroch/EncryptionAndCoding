package pl.model.TripleDES;

import pl.model.Operations.BitwiseOperations;

public class DES {

    private byte[] key;
    private byte[][] turnkey;

    private final static byte[] permutationPC1 = {57, 49, 41, 33, 25, 17,  9,
                   1, 58, 50, 42, 34, 26, 18,
                  10,  2, 59, 51, 43, 35, 27,
                  19, 11,  3, 60, 52, 44, 36,
                  63, 55, 47, 39, 31, 23, 15,
                   7, 62, 54, 46, 38, 30, 22,
                  14,  6, 61, 53, 45, 37, 29,
                  21, 13,  5, 28, 20, 12,  4};

    private final static byte[] permutationPC2 = {14, 17, 11, 24,  1,  5,  3, 28,
                  15,  6, 21, 10, 23, 19, 12,  4,
                  26,  8, 16,  7, 27, 20, 13,  2,
                  41, 52, 31, 37, 47, 55, 30, 40,
                  51, 45, 33, 48, 44, 49, 39, 56,
                  34, 53, 46, 42, 50, 36, 29, 32};

    private final static byte[] shifts = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    private final static byte [] expandingPermutation = { 32,   1,  2,  3,  4,  5,  4,  5,
                    6,   7,  8,  9,  8,  9, 10, 11,
                   12,  13, 12, 13, 14, 15, 16, 17,
                   16,  17, 18, 19, 20, 21, 20, 21,
                   22,  23, 24, 25, 24, 25, 26, 27,
                   28,  29, 28, 29, 30, 31, 32,  1};

    private final static byte[] pBlock = {16, 7, 20, 21, 29, 12, 28, 17,
             1, 15, 23, 26,  5, 18, 31, 10,
             2,  8, 24, 14, 32, 27,  3,  9,
            19, 13, 30,  6, 22, 11,  4, 25};

    private final byte[][] sBoxes = {
            { 14,  4, 13, 1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9, 0,  7,
               0, 15,  7, 4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5, 3,  8,
               4,  1, 14, 8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10, 5,  0,
              15, 12,  8, 2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0, 6, 13},

            { 15,  1,  8, 14,  6, 11,  3,  4,  9, 7,  2, 13, 12, 0, 5,  10,
               3, 13,  4,  7, 15,  2,  8, 14, 12, 0,  1, 10,  6, 9, 11,  5,
               0, 14,  7, 11, 10,  4, 13,  1,  5, 8, 12,  6,  9, 3,  2, 15,
              13,  8, 10,  1,  3, 15,  4,  2, 11, 6,  7, 12,  0, 5, 14,  9},

            { 10,  0,  9, 14, 6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8,
              13,  7,  0,  9, 3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1,
              13,  6,  4,  9, 8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7,
               1, 10, 13,  0, 6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12},

            { 7, 13, 14, 3,  0,  6,  9, 10,  1, 2, 8,  5, 11, 12,  4, 15,
             13,  8, 11, 5,  6, 15,  0,  3,  4, 7, 2, 12,  1, 10, 14,  9,
             10,  6,  9, 0, 12, 11,  7, 13, 15, 1, 3, 14,  5,  2,  8,  4,
             3,  15,  0, 6, 10,  1, 13,  8,  9, 4, 5, 11, 12,  7,  2, 14},

            { 2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13, 0, 14,  9,
             14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3, 9,  8,  6,
              4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6, 3,  0, 14,
             11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10, 4,  5,  3},

            { 12,  1, 10, 15, 9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11,
              10, 15,  4,  2, 7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8,
               9, 14, 15,  5, 2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6,
               4,  3,  2, 12, 9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13},

            { 4, 11,  2, 14, 15, 0,  8, 13,  3, 12, 9,  7,  5, 10, 6,  1,
             13,  0, 11,  7,  4, 9,  1, 10, 14,  3, 5, 12,  2, 15, 8,  6,
              1,  4, 11, 13, 12, 3,  7, 14, 10, 15, 6,  8,  0,  5, 9,  2,
              6, 11, 13,  8,  1, 4, 10,  7,  9,  5, 0, 15, 14,  2, 3, 12},

            { 13,  2,  8, 4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7,
               1, 15, 13, 8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2,
               7, 11,  4, 1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8,
               2,  1, 14, 7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11}
    };


    public DES(byte[] key) {
        this.key = key;
    }

    public DES() {}


    public byte[] pBlockPermutation(byte[] in){

        return BitwiseOperations.permutation(in, pBlock,4 );
    }


    public byte[] expandingPermutation(byte[] in){

        return BitwiseOperations.permutation(in, expandingPermutation, 6);

    }


    public byte[] PC1permutation(byte[] in){

        return BitwiseOperations.permutation(in, permutationPC1, 7);

    }


    public byte[] PC2permutation(byte[] in){

        return BitwiseOperations.permutation(in, permutationPC2, 6);

    }


    public void generateTurnkeys(){

        byte[] key56 = PC1permutation(key);
        byte[] leftKey = BitwiseOperations.copyBits(key56,0,28,4);
        byte[] rightKey = BitwiseOperations.copyBits(key56,28,28,4);
        byte[][] generatedKeys = new byte[16][48];
        byte[] keysAfterShift;

        for (int i = 0; i < 16; i++) {
            leftKey = BitwiseOperations.shiftLeft(leftKey, shifts[i]);
            rightKey = BitwiseOperations.shiftLeft(rightKey, shifts[i]);
            keysAfterShift = BitwiseOperations.mergeTwoArrays(leftKey,rightKey,28,7);
            generatedKeys[i] = PC2permutation(keysAfterShift);

        }
        turnkey = generatedKeys;

    }


    public byte[] SBoxesOperations(byte[] in){

        byte[] out = new byte[4];
        byte[] col = {0};
        byte[] row = {0};
        for(int i = 0; i < 8; i++){
            byte[] six = BitwiseOperations.return6Bits(in,i);
            byte[] four = new byte[1];

             BitwiseOperations.setBit(row,6, BitwiseOperations.getBit(six,2));
             BitwiseOperations.setBit(row,7, BitwiseOperations.getBit(six,7));
             BitwiseOperations.setBit(col,4, BitwiseOperations.getBit(six,3));
             BitwiseOperations.setBit(col,5, BitwiseOperations.getBit(six,4));
             BitwiseOperations.setBit(col,6, BitwiseOperations.getBit(six,5));
             BitwiseOperations.setBit(col,7, BitwiseOperations.getBit(six,6));


             four[0] = sBoxes[i][16 * row[0] + col[0]];

             BitwiseOperations.setBit(out, (i * 4), BitwiseOperations.getBit(four,4));
             BitwiseOperations.setBit(out,1 + (i * 4), BitwiseOperations.getBit(four,5));
             BitwiseOperations.setBit(out,2 + (i * 4), BitwiseOperations.getBit(four,6));
             BitwiseOperations.setBit(out,3 + (i * 4), BitwiseOperations.getBit(four,7));



        }

        return out;
    }

    public byte[] encodeBlock(byte[] in){



        byte[] l = BitwiseOperations.copyBits(in,0,32,4);
        byte[] r = BitwiseOperations.copyBits(in,32,32,4);

        for(int i = 0; i < 16; i++){
            byte[] roundResult = round(l,r,i);
            l = r;
            r = roundResult;
        }
        return BitwiseOperations.mergeTwoArrays(r,l,32 ,8 );

    }


    public byte[] round(byte[] l, byte[] p, int i){
        byte[] r = expandingPermutation(p);
        byte[] xor = BitwiseOperations.XOR(r, turnkey[i],6 );
        byte[] s = SBoxesOperations(xor);
        byte[] pp = pBlockPermutation(s);
        return  BitwiseOperations.XOR(pp,l, 4);

    }

    public byte[] decodeBlock(byte[] in){

        byte[] l = BitwiseOperations.copyBits(in,0,32,4);
        byte[] r = BitwiseOperations.copyBits(in,32,32,4);

        for(int i = 15; i >= 0; i--){
            byte[] roundResult = round(l,r,i);
            l = r;
            r = roundResult;
        }
        return BitwiseOperations.mergeTwoArrays(r,l,32 ,8 );

    }


}
