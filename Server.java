// import java.math.BigInteger;
// import java.net.ServerSocket;
// import java.util.Scanner;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
//import java.util.function.IntToDoubleFunction;

public class Server {
    public static void main(String[] args) 
    {
        Socket sock = null; 
        ServerSocket server=null; 
        try
        {
            server = new ServerSocket(12345);
            while(true){
                System.out.println("Waiting for a client...");
                sock = server.accept(); 
                System.out.println("Client connected");
                DataInputStream in = new DataInputStream(sock.getInputStream());
                DataOutputStream out = new DataOutputStream(sock.getOutputStream());
                
            }
        }
        catch(IOException e) {}
    }
}
