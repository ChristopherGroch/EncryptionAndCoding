package pl.model.HuffmanCoding;

import pl.model.Operations.FileOperations;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket serverSocket;
    private static Socket sock;
    private static DataInputStream is;

    public static final int PORT = 5050;


    private static void ReceiveFile(String filepath) throws IOException{
        int length = is.readInt();
        if (length > 0) {
            byte[] bytes = new byte[length];
            is.readFully(bytes);
            FileOperations.saveBytesToFile(filepath,bytes);

            is.close();
            sock.close();
            serverSocket.close();
        }
    }

    public static void StartReceiving(String filepath){
        try {
            serverSocket = new ServerSocket(PORT);
            sock = serverSocket.accept();
            is = new DataInputStream(sock.getInputStream());
            ReceiveFile(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
