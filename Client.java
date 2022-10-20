// import java.math.BigInteger;
// import java.net.ServerSocket;
// import java.util.Scanner;
import java.io.*;
// import java.net.ServerSocket;
import java.net.Socket;
//import java.util.function.IntToDoubleFunction;

public class Client
{  
    public static void main(String[] args) 
    {
        Socket sock = null; 
        try
        {
            System.out.println("Connecting to server..."); 
            sock = new Socket("localhost",12345); 
            System.out.println("Connected");
            DataInputStream in = new DataInputStream(sock.getInputStream());
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());

        }
        catch(IOException e) {}
    }
}