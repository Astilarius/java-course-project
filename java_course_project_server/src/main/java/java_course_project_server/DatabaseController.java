package java_course_project_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import Models.Account;
import Models.Expense;
import Models.ExpenseDeserializer;
import Models.Project;

public class DatabaseController {
    private static final String url = "jdbc:mysql://localhost:3306/sys";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public static boolean verifyAccount(String name, boolean verify){
        String query = "UPDATE accounts SET verified="+verify+" WHERE accountName='"+name+"'";
       
        System.out.println(query);
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            try { con.close(); } catch(SQLException se) {  }
            try { stmt.close(); } catch(SQLException se) {  }
        }
    }
    public static boolean writeAccount(Account a){
        String query = "INSERT INTO accounts(accountName, hash, salt, verified) VALUES ('"+a.getAccountName()+"', '"+a.getHash()+"', '"+a.getSalt()+"', "+a.isVerified()+")";
       
        System.out.println(query);
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            try { con.close(); } catch(SQLException se) {  }
            try { stmt.close(); } catch(SQLException se) {  }
        }
    }
    public static Account getAccountByName(String name){
        String query = "select * from accounts where accountName='"+name+"'";
        System.out.println(query);
        Account res = new Account();
        res.setVerified(false);
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                res.setId(rs.getInt(1));
                res.setAccountName(rs.getString(2));
                res.setHash(rs.getString(3));
                res.setSalt(rs.getString(4));
                res.setVerified(rs.getBoolean(5));
            } 
            return res;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return res;
    }
    public static Collection<Account> getAccounts(){
        String query = "select * from accounts";
        System.out.println(query);
        Collection<Account> accounts = new ArrayList<Account>();
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                Account temp = new Account();
                temp.setId(rs.getInt(1));
                temp.setAccountName(rs.getString(2));
                temp.setHash(rs.getString(3));
                temp.setSalt(rs.getString(4));
                temp.setVerified(rs.getBoolean(5));
                accounts.add(temp);
            } 
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } catch (JsonSyntaxException e){
            e.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return accounts;
    }

    public static boolean writeProject(Project p){
        Gson gson = new Gson();
        String json = gson.toJson(p);
        String query = "INSERT INTO projects(name, isPublic, author, price, json) VALUES ('"+
        p.getName()+"', "+
        p.isPublic()+", '"+
        p.getAuthor().getAccountName()+"', "+
        p.getPrice()+", '"+
        json+"')";
       
        System.out.println(query);
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();

            stmt.execute(query);
            return true;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }
       
    public static Project getLastProject(){
        String query = "SELECT * FROM projects ORDER BY id DESC LIMIT 0, 1";
        System.out.println(query);
        Project res = new Project();
        String resJson="";
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                resJson=rs.getString(6);
                Gson gson = new Gson();
                res = gson.fromJson(resJson, Project.class);
                res.setId(rs.getInt(1));
                System.out.println(res.id);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return res;
    }
       
    public static Project getProjectById(Integer id){
        String query = "select * from projects where id="+id;
        System.out.println(query);
        Project res = new Project();
        String json="";
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Expense.class, new ExpenseDeserializer());
            builder.setPrettyPrinting();
            Gson gson = builder.create();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                json=rs.getString(6);
                res = gson.fromJson(json, Project.class);
                res.setId(rs.getInt(1));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return res;
    }
    public static Collection<Project> getProjectsByAuthor(String author){
        String query = "select * from projects where author='"+author+"'";
        System.out.println(query);
        Project temp = new Project();
        Collection<Project> projects = new ArrayList<Project>();
        String json="";
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Expense.class, new ExpenseDeserializer());
            builder.setPrettyPrinting();
            Gson gson = builder.create();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                json=rs.getString(6);
                temp = gson.fromJson(json, Project.class);
                temp.setId(rs.getInt(1));
                projects.add(temp);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } catch (JsonSyntaxException e){
            e.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return projects;
    }
    public static Collection<Project> getPublicProjects(String author){
        String query = "select * from projects where isPublic=true AND author!='"+author+"'";
        System.out.println(query);
        Project temp = new Project();
        Collection<Project> projects = new ArrayList<Project>();
        String json="";
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();

            rs = stmt.executeQuery(query);
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Expense.class, new ExpenseDeserializer());
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            while (rs.next()){
                json=rs.getString(6);
                temp = gson.fromJson(json, Project.class);
                temp.setId(rs.getInt(1));
                projects.add(temp);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } catch (JsonSyntaxException e){
            e.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return projects;
    }
    public static Collection<Project> getWhitelistedProjects(String author){
        String query = "select * from projects";
        System.out.println(query);
        Project temp = new Project();
        Collection<Project> projects = new ArrayList<Project>();
        String json="";
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();

            rs = stmt.executeQuery(query);
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Expense.class, new ExpenseDeserializer());
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            while (rs.next()){
                json=rs.getString(6);
                temp = gson.fromJson(json, Project.class);
                temp.setId(rs.getInt(1));
                if(temp.getWhiteList().contains(author)){
                    projects.add(temp);
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } catch (JsonSyntaxException e){
            e.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return projects;
    }
    public static boolean updateProject(Project p){
        Gson gson = new Gson();
        String json = gson.toJson(p);
        String query = "UPDATE projects SET name='"+
        p.getName()+"', isPublic="+
        p.isPublic()+", author='"+
        p.getAuthor().getAccountName()+"', price="+
        p.getPrice()+", json='"+
        json+"' WHERE id="+p.getId();
       
        System.out.println(query);
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            try { con.close(); } catch(SQLException se) {  }
            try { stmt.close(); } catch(SQLException se) {  }
        }
    }
    public static boolean deleteProject(Project p){
        p.setId( (p.getId()*-1) - 1 );
        String query = "DELETE FROM projects WHERE id="+p.getId();
       
        System.out.println(query);
        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            try { con.close(); } catch(SQLException se) {  }
            try { stmt.close(); } catch(SQLException se) {  }
        }
    }
}
