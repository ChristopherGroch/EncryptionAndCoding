package pl.model.HuffmanCoding;

import pl.model.Operations.FileOperations;
import java.io.*;
import java.net.Socket;

public class Client {


    private static Socket sock;

    private static void SocketInit(String host) throws IOException {
        sock = new Socket(host,Server.PORT);
    }

    private static void SendFileThroughSocket() throws IOException {
        byte[] fileInBytes = FileOperations.loadBytesFromFile("Model//result.txt");
        DataOutputStream os =new DataOutputStream(sock.getOutputStream());
        if (fileInBytes != null) {
            os.writeInt(fileInBytes.length);

            os.write(fileInBytes);
            os.close();
            sock.close();
            File file = new File("Model//result.txt");
            file.delete();
        }
    }

    public static void SendingMessage(String ipAddress,File file) {
        try {

            SocketInit(ipAddress);

            CodeHuffman huff = new CodeHuffman();
            huff.innitHuffmanWithFile(file.getAbsolutePath());
            String code = huff.codeToString();
            FileOperations.saveStringAsBitsToFile("Model//result.txt",code);
            SendFileThroughSocket();
        } catch (IOException e ) {
            e.printStackTrace();

        }
    }


}
