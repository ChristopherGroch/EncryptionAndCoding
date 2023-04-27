package pl.model.Operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class OperacjeTest {

    @Test
    void getBit() {
        byte b1 = 1;
        Assertions.assertEquals(BitwiseOperations.getBit(b1,0),0);
        Assertions.assertEquals(BitwiseOperations.getBit(b1,7),1);
        Assertions.assertEquals(BitwiseOperations.getBit(b1,8),0);

    }

    @Test
    void setBit() {
        byte b1 = 1;
        b1 = BitwiseOperations.setBit(b1,7,0);
        Assertions.assertEquals(b1,0);
        b1 = BitwiseOperations.setBit(b1,7,0);
        Assertions.assertEquals(b1,0);
        b1 = BitwiseOperations.setBit(b1,6,1);
        Assertions.assertEquals(b1,2);
        b1 = BitwiseOperations.setBit(b1,6,1);
        Assertions.assertEquals(b1,2);
        b1 = BitwiseOperations.setBit(b1,8,1);
        Assertions.assertEquals(b1,2);

    }

    @Test
    void testGetBit() {
        byte[] b1 = {1};
        Assertions.assertEquals(BitwiseOperations.getBit(b1,0),0);
        Assertions.assertEquals(BitwiseOperations.getBit(b1,7),1);
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class,()-> BitwiseOperations.getBit(b1,8));
    }

    @Test
    void testSetBit() {
        byte[] b1 = {1};
        BitwiseOperations.setBit(b1,7,0);
        Assertions.assertEquals(b1[0],0);
        BitwiseOperations.setBit(b1,7,0);
        Assertions.assertEquals(b1[0],0);
        BitwiseOperations.setBit(b1,6,1);
        Assertions.assertEquals(b1[0],2);
        BitwiseOperations.setBit(b1,6,1);
        Assertions.assertEquals(b1[0],2);
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class,()-> BitwiseOperations.setBit(b1,8,1));

    }

    @Test
    void skopiujBity() {
        byte[] b1 = {(byte)255};
        byte[] b2 = BitwiseOperations.copyBits(b1,1,1,1);
        Assertions.assertEquals(b2.length,1);
        Assertions.assertEquals(b2[0],-128);

    }

    @Test
    void polaczDwieTablice() {
        byte[] b1 = {(byte)3,(byte) 2};
        byte[] b2 = {(byte)5,(byte) 7,(byte) 9};
        byte[] b3 = BitwiseOperations.mergeTwoArrays(b1,b2,16,5);
        Assertions.assertEquals(b3.length,5);
        Assertions.assertEquals(b3[0],3);
        Assertions.assertEquals(b3[1],2);
        Assertions.assertEquals(b3[2],5);
        Assertions.assertEquals(b3[3],7);
        Assertions.assertEquals(b3[4],0);
    }
}