package pl.model.Operations;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileOperations {

    public static byte[] loadBytesFromFile(String file){
        try (
                InputStream sin = new FileInputStream(file)
        ) {

            long size = new File(file).length();
            byte[] out = new byte[(int) size];

            sin.read(out);

            return out;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
    public static void saveBytesToFile(String file, byte[] in){
        try (
                OutputStream sou = new FileOutputStream(file)
        ) {
            sou.write(in);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String loadStringFromFile(String file){
        try(InputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))){

            String str;
            StringBuilder s = new StringBuilder();

            while ((str = br.readLine()) != null){
                s.append(str);
                s.append('\n');
            }
            return s.toString();
        }catch (Exception e ){
            System.out.println(file);
        }
        return null;
    }

    public static void saveStringAsBitsToFile(String file, String in){
        char[] chars = in.toCharArray();
        int rest = chars.length % 16;
        boolean divisibility = rest == 0;
        int maxIndex = (chars.length - 1) / 16 + 1;
        byte[] bytes;
        if(divisibility){
            bytes = new byte[maxIndex * 2];
        } else {
            bytes = new byte[maxIndex * 2 + 1];
            bytes[maxIndex * 2] = (byte) (chars.length % 8);
        }
        for(int i =0;i<maxIndex * 16;i++){
            if (chars.length > i){
                BitwiseOperations.setBit(bytes,i,chars[i] - '0');
            }else {
                BitwiseOperations.setBit(bytes,i,0);
            }
        }
        try (
                OutputStream sou = new FileOutputStream(file)
        ) {
            sou.write(bytes);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static String loadBitsAsStringFromFile(String file){
        try (
                InputStream sin = new FileInputStream(file)
        ) {

            long size = new File(file).length();
            byte[] bytes = new byte[(int) size];
            sin.read(bytes);
            boolean divisibility = bytes.length % 2 == 0;
            int rest;

            char[] chars;
            if(!divisibility) {
                rest = bytes[bytes.length - 1];
                chars = new char[(bytes.length - 1) * 8 - 8 + rest];
            } else {
                chars = new char[bytes.length * 8];
            }
            for(int i =0;i< chars.length;i++){
                chars[i] =(char) ('0' + BitwiseOperations.getBit(bytes,i));
            }
            return String.valueOf(chars);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void saveStringAsString(String file, String in){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(in);

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
