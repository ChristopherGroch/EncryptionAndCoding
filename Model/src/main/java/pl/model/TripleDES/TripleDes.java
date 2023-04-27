package pl.model.TripleDES;

import pl.model.Operations.BitwiseOperations;

import java.io.IOException;


public class TripleDes {

    private DES DES1;
    private DES DES2;
    private DES DES3;


    public TripleDes(){

    }


    public void setKeys(byte[] key1, byte[] key2, byte[] key3) throws IOException{
        if (key1.length != 8 || key2.length != 8 || key3.length != 8) {
            throw new IOException("Key should be 8 bytes long (UTF-8 encoding)");
        }
        DES1 = new DES(key1);
        DES2 = new DES(key2);
        DES3 = new DES(key3);
        DES1.generateTurnkeys();
        DES2.generateTurnkeys();
        DES3.generateTurnkeys();
    }


    public byte[] encodeMessage(byte[] in) {
        int maxIndex = in.length / 8;
        boolean divisibility = in.length % 8 == 0;
        byte[] out;

        if(divisibility){
            out = new byte[in.length];
        } else {
            out = new byte[maxIndex * 8 + 9];
        }


        byte[] cipher;

        for(int i = 0; i <= maxIndex; i++){

            if(i == maxIndex && divisibility){
                break;
            }

            cipher = DES1.encodeBlock(BitwiseOperations.return64Bits(i,in));
            cipher = DES2.decodeBlock(cipher);
            cipher = DES3.encodeBlock(cipher);

            for(int j = 0; j < 64; j++){
                BitwiseOperations.setBit(out,j + (i * 64), BitwiseOperations.getBit(cipher,j));
            }

        }

        if(!divisibility){
            out[maxIndex * 8 + 8] = (byte)( 8 - in.length + maxIndex * 8);
        }



        return out;
    }


    public byte[] decodeMessage(byte[] in){
        int maxIndex = in.length / 8;
        boolean divisibility = in.length % 8 == 0;
        byte[] out;
        int delta = 0;

        if(divisibility){
            out = new byte[in.length];
        } else {
            delta = in[maxIndex * 8];
            out = new byte[maxIndex * 8 - delta];
        }

        byte[] decipher;

        for(int i = 0; i < maxIndex; i++){

            decipher = DES3.decodeBlock(BitwiseOperations.return64Bits(i,in));
            decipher = DES2.encodeBlock(decipher);
            decipher = DES1.decodeBlock(decipher);

            if(i == maxIndex - 1){
                for(int j = 0;j < (8-delta)*8; j++){
                    BitwiseOperations.setBit(out,j + (i * 64), BitwiseOperations.getBit(decipher,j));
                }
            }else {

                for (int j = 0; j < 64; j++) {
                    BitwiseOperations.setBit(out, j + (i * 64), BitwiseOperations.getBit(decipher, j));
                }
            }
        }

        return out;
    }


}
