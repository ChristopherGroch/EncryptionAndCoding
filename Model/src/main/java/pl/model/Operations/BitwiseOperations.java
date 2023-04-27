package pl.model.Operations;


import java.math.BigInteger;
import java.util.Arrays;

public class BitwiseOperations {


    public static byte[] permutation(byte[] in, byte[] permutation, int howMuchBytes){

        byte[] out = new byte[howMuchBytes];

        for (int i = 0; i < permutation.length; i++) {

            setBit(out,i,getBit(in, permutation[i]-1));

        }

        return out;
    }
    public static int getBit(byte in, int pos){

        return ((in << (pos)) & 128) > 0 ? 1 : 0;
    }

    public static byte setBit(byte in, int whichBit, int value){

        if(value == 0){
            in = (byte)(~(128 >> whichBit) & in);
        }else {
            in = (byte)((128 >> whichBit) | in);
        }
        return in;
    }

    public static int getBit(byte[] in, int pos){

        int whichByte = pos / 8;
        int whichBit = pos % 8;
        byte workingByte = in[whichByte];
        return ((workingByte << (whichBit)) & 128) > 0 ? 1 : 0;
    }


    public static void setBit(byte[] in, int pos, int value){

        int whichByte = pos / 8;
        int whichBit = pos % 8;
        byte workingByte = in[whichByte];
        if(value == 0){
            workingByte = (byte)(~(128 >> whichBit) & workingByte);
        }else {
            workingByte = (byte)((128 >> whichBit) | workingByte);
        }
        in[whichByte] = workingByte;


    }


    public static byte[] copyBits(byte[] in, int from, int size, int howMuchBytes) {

        byte[] out = new byte[howMuchBytes];

        for (int i = 0; i < size; i++) {

            setBit(out,i, getBit(in,from+i));

        }

        return out;
    }


    public static byte[] shiftLeft(byte[] in, int shift){

        byte[] out = Arrays.copyOf(in,4);

        for(int i = 0; i < shift; i++){

            int bit = BitwiseOperations.getBit(out,0);

            for(int j = 0; j < 27; j++){
                setBit(out,j,getBit(out,(j + 1)));

            }
            setBit(out,27,bit);
        }

        return out;
    }


    public static byte[] return6Bits(byte[] in, int number){
        byte[] out = {0};

        for(int i = 0; i < in.length; i++){
            BitwiseOperations.setBit(out,i + 2, BitwiseOperations.getBit(in,i + (number * 6)));
        }

        return out;
    }


    public static byte[] mergeTwoArrays(byte[] l, byte[] p, int size, int howMuchBytes){

        byte[] out = new byte[howMuchBytes];

        for(int i = 0; i < size; i++){
            BitwiseOperations.setBit(out,i, BitwiseOperations.getBit(l,i));
        }

        for(int i = 0;i < size; i++){
            BitwiseOperations.setBit(out,size + i, BitwiseOperations.getBit(p,i));
        }

        return out;
    }


    public static byte[] XOR(byte[] bytes1, byte[] bytes2, int howMuchBytes){

        byte[] out = new byte[howMuchBytes];

        for(int i = 0; i < howMuchBytes; i++){
            out[i] = (byte)(bytes1[i] ^ bytes2[i]);
        }

        return out;

    }


    public static byte[] return64Bits(int number, byte[] in){

        int maxIndex = in.length / 8;

        if(number > maxIndex){
            return null;
        }

        byte[] out = new byte[8];

        if((in.length % 8 == 0) || (number < maxIndex)) {
            System.arraycopy(in, (number * 8), out, 0, 8);

        }else{
            int delta = in.length - (number * 8);
            if (delta >= 0) System.arraycopy(in, (number * 8), out, 0, delta);

            for(int i = delta; i < 8; i++){
                out[i] = 0;
            }
        }
        return out;
    }
    public static byte[] bigToArray(BigInteger big,int bytes){

        if(big.toByteArray().length > bytes){
            return trim(big.toByteArray());
        } else if (big.toByteArray().length < bytes) {
            byte[] array = new byte[bytes];
            byte[] array2 = big.toByteArray();
            if (bytes - (bytes - array2.length) >= 0)
                System.arraycopy(array2, 0, array, bytes - array2.length, bytes - (bytes - array2.length));
            return array;
        } else {
            return big.toByteArray();
        }

    }
    public static byte[] trim(byte[] trimmed){

        if (trimmed[0] != 0){
            return trimmed;
        }
        byte[]array= new byte[trimmed.length-1];
        System.arraycopy(trimmed,1,array,0,trimmed.length-1);

        return array;
    }

    public static  byte[] returnBytes(int number, byte[] in, int size){
        boolean divisibility = false;
        int maxIndex = in.length / size;
        int rest = in.length % size;
        byte [] out = new byte[size];
        if (in.length % 34 == 0){
            divisibility = true;
        }
        if(!divisibility && number == maxIndex){
            out = new byte[rest];
            System.arraycopy(in, (number * size), out, 0, rest);
            return out;
        }
        System.arraycopy(in, (number * size), out, 0, size);

        return out;
    }

}
