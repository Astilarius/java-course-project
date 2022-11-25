package Server;
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
        Double result = 0.0;
        try
        {
            server = new ServerSocket(12345);
            while(true){
                System.out.println("Waiting for a client...");
                sock = server.accept(); 
                System.out.println("Client connected");
                DataInputStream in = new DataInputStream(sock.getInputStream());
                DataOutputStream out = new DataOutputStream(sock.getOutputStream());
                String tempName;
                Double tempD;
                Integer tempI;
                Integer msg = 1;
                while (msg != 0){
                    msg = in.readInt();
                    System.out.println(msg);
                    if (msg == 1){
                        System.out.println("IN M!");
                        tempName = in.readUTF();
                        tempD = in.readDouble();
                        tempI = in.readInt();
                        result += tempD*tempI;
                        System.out.println("name "+tempName+" price "+tempD+" amount "+tempI);
                    }
                    if (msg == 2){
                        System.out.println("IN P!");
                        tempName = in.readUTF();
                        tempI = in.readInt();
                        tempD = in.readDouble();
                        result += tempD*tempI;
                        System.out.println("name "+tempName+" price per unit "+tempD+" amount "+tempI);
                    }
                }
                System.out.println("total price - "+result);
            }
        }
        catch(IOException e) {}
    }
}
