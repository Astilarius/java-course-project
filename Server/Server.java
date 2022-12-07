package Server;
// import java.math.BigInteger;
// import java.net.ServerSocket;
// import java.util.Scanner;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.function.IntToDoubleFunction;

public class Server {
    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/sys";
    private static final String user = "root";
    private static final String password = "root";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    private static int maxId = 0;
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
                String projectName="";
                String msg = in.readUTF();
                String dataString = "";
                System.out.println(msg);
                if (msg.startsWith("manage")){
                    String query = "select * from kusach";
                    System.out.println(query);
                    try {
                        // opening database connection to MySQL server
                        con = DriverManager.getConnection(url, user, password);
            
                        stmt = con.createStatement();

                        rs = stmt.executeQuery(query);
                        maxId = 0;
                        while(rs.next()){
                            out.writeInt(rs.getInt(1));
                            System.out.println(rs.getInt(1));

                            out.writeUTF(rs.getString(2));
                            System.out.println(rs.getString(2));

                            out.writeDouble(rs.getDouble(3));
                            System.out.println(rs.getDouble(3));

                            out.writeUTF(rs.getString(4));
                            System.out.println(rs.getString(4));
                            maxId++;
                        }
                        out.writeInt(-1);

                    } catch (SQLException sqlEx) {
                        sqlEx.printStackTrace();
                    } finally {
                        //close connection ,stmt and resultset here
                        try { con.close(); } catch(SQLException se) { /*can't do anything */ }
                        try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
                    }
                } else if (msg.startsWith("write")) {
                    while (msg != "end"){
                        msg = in.readUTF();
                        if (msg.startsWith("end")) break;
                        dataString+=msg;
                        String[] parsedMsg = msg.split(" ");
                        System.out.println(msg);
                        if (Integer.parseInt(parsedMsg[0]) == 1){
                            tempName = parsedMsg[1];
                            tempD = Double.parseDouble(parsedMsg[2]);
                            tempI = Integer.parseInt(parsedMsg[3]);
                            result += tempD*tempI;
                            System.out.println("subres = "+result);
                            System.out.println("name - "+tempName+", price - "+tempD+", amount - "+tempI+", id - "+parsedMsg[4]);
                        } else if (Integer.parseInt(parsedMsg[0]) == 2){
                            if (parsedMsg[1] == "") break;
                            tempName = parsedMsg[1];
                            projectName = tempName;
                            tempD = Double.parseDouble(parsedMsg[2]);
                            tempI = Integer.parseInt(parsedMsg[3]);
                            result += tempD*tempI;
                            System.out.println("name - "+tempName+", price per unit - "+tempD+", amount - "+tempI+", id - "+parsedMsg[4]+", linked elements - "+parsedMsg[5]);
                        }
                    }
                    System.out.println("total price - "+result);
                    System.out.println("dataString - "+dataString);
                    out.writeDouble(result);

                
                    String query = "INSERT INTO kusach(id, name, result, dataString) VALUES ("
                    + getMaxId() + ", '" 
                    + projectName + "', '"
                    + result + "', '"
                    + dataString + "')";
                    maxId++;
                    System.out.println(query);
                    try {
                        // opening database connection to MySQL server
                        con = DriverManager.getConnection(url, user, password);
            
                        // getting Statement object to execute query
                        stmt = con.createStatement();
            
                        // executing query
                        stmt.execute(query);
                        
                    } catch (SQLException sqlEx) {
                        sqlEx.printStackTrace();
                    } finally {
                        //close connection ,stmt and resultset here
                        try { con.close(); } catch(SQLException se) { /*can't do anything */ }
                        try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
                    }
                } else if (msg.startsWith("delet")){

                    try {
                        String query = "delete from kusach where id="+msg.split(" ")[1];
    
                        con = DriverManager.getConnection(url, user, password);

                        stmt = con.createStatement();
            
                        stmt.execute(query);
                        
                    } catch (SQLException sqlEx) {
                        sqlEx.printStackTrace();
                    } finally {
                        //close connection ,stmt and resultset here
                        try { con.close(); } catch(SQLException se) { /*can't do anything */ }
                        try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
                    }
                }
            }
        }
        catch(IOException e) {}
    }
    private static Integer getMaxId(){
        
        String query = "select * from kusach";
        try {
            Class.forName("com.mysql.jdbc.Driver"); 
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();

            rs = stmt.executeQuery(query);

            maxId = 0;
            while(rs.next()){
                maxId++;
            }
            maxId++;
        } catch (ClassNotFoundException sqlEx) {
            sqlEx.printStackTrace();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return maxId;
    }
}
