package pl.model.ElGamal;

import pl.model.Operations.BitwiseOperations;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class ElGamal {
    public BigInteger p;
    public BigInteger g;
    public BigInteger x;
    public BigInteger y;
    public BigInteger pMinus1;
    public int pLength = 264;

    public String getP() {
        return p.toString(16);
    }

    public String getG() {
        return g.toString(16);
    }

    public String getX() {
        return x.toString(16);
    }

    public String getY() {
        return y.toString(16);
    }

    public ElGamal() {
        generateKeys();
    }

    public void generateKeys() {
        this.p = BigInteger.probablePrime(pLength, new Random());

        this.g = new BigInteger(pLength -5,new Random());

        this.x = new BigInteger(pLength -5,new Random());

        this.pMinus1 = p.subtract(BigInteger.ONE);

        this.y = this.g.modPow(x,p);

    }

    public void setKeys(BigInteger p, BigInteger g, BigInteger x, BigInteger y) throws IOException {

        if (p.compareTo(this.p) == 0 && g.compareTo(this.g) == 0 && x.compareTo(this.x) == 0 && y.compareTo(this.y) == 0) {
            return;
        }

        if (p.bitLength() != pLength) {
            throw new IOException("p must be 264 bits long");
        } else if (!p.isProbablePrime(5)) {
            throw new IOException("p must be prime number");
        } else if (p.compareTo(g) < 0) {
            throw new IOException("g must be less than p - 1");
        } else if (p.compareTo(x) < 0)  {
            throw new IOException("x must be less than p - 1");
        } else if (y.compareTo(g.modPow(x,p)) != 0) {
            throw new IOException("y must be equal to g^x mod p");
        }

        this.p = p;
        this.pMinus1 = p.subtract(BigInteger.ONE);
        this.g = g;
        this.x = x;
        this.y = y;
    }

    public byte[] encryptBlock(byte[] in, BigInteger k){

        BigInteger c2 = new BigInteger(1,in).multiply(y.modPow(k,p)).mod(p);
        return BitwiseOperations.bigToArray(c2,33);
    }
    public byte[] decryptBlock(BigInteger c1, BigInteger c2){

        BigInteger result = c2.multiply(c1.modPow(x,p).modInverse(p)).mod(p);
        return BitwiseOperations.bigToArray(result,32);
    }
    public byte[] encryptMessage(byte[] in){

        BigInteger k;
        do{
            k = new BigInteger(pLength -1,new Random());

        }while(!k.gcd(pMinus1).equals(BigInteger.ONE));

        BigInteger c1 = g.modPow(k,p);

        int maxIndex = in.length / 32;
        int howMuchZeroes = 32 - in.length % 32;
        byte [] out;
        if(in.length % 32 == 0){
            out = new byte[maxIndex * 33 + 33];
            maxIndex--;
        }else {
            out = new byte[(maxIndex + 1) * 33 + 33 + 1];
            out[(maxIndex + 1) * 33 + 33 ] = (byte) howMuchZeroes;
        }
        byte[] cipher;
        byte[] C = BitwiseOperations.bigToArray(c1,33);
        for(int i = 0;i<264;i++){
            BitwiseOperations.setBit(out,i, BitwiseOperations.getBit(C,i));
        }
        for(int i = 0;i<=maxIndex;i++){
            cipher = encryptBlock(BitwiseOperations.returnBytes(i,in,32), k);
            for(int j =0;j<264;j++){
                BitwiseOperations.setBit(out,264 + j+(i * 264), BitwiseOperations.getBit(cipher,j));
            }
        }

        return out;
    }

    public byte[] decryptMessage(byte[] in){
        int howMuchZeroes = 0;
        int maxIndex = in.length / 33 - 1;
        if(in.length % 33 != 0){
            maxIndex = (in.length - 1)/ 33 - 1 ;
            howMuchZeroes = in[(maxIndex+1) * 33];
        }
        int size = ((maxIndex) * 32 - howMuchZeroes);

        byte[] out = new byte[size];


        byte[] help = new byte[33];
        for (int i = 0;i<264;i++){
            BitwiseOperations.setBit(help,i, BitwiseOperations.getBit(in,i));

        }
        BigInteger C1 = new BigInteger(1,help);


        for(int i =1;i<=maxIndex;i++){
            help = decryptBlock(C1,new BigInteger(1, BitwiseOperations.returnBytes(i,in,33)));
            if(i == maxIndex && in.length % 33 != 0){

                for(int j = 0;j< 256 - (howMuchZeroes * 8);j++){
                    BitwiseOperations.setBit(out,j+((i -1) * 256), BitwiseOperations.getBit(help,j + (howMuchZeroes * 8)));
                }

            }else{
                for(int j =0;j<256;j++) {
                    BitwiseOperations.setBit(out,j+((i -1) * 256), BitwiseOperations.getBit(help,j));
                }
            }
        }


        return out;
    }

}