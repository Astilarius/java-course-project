package Models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java_course_project_server.App;

public class Project {
    public Collection<Expense> expenses = new ArrayList<Expense>();
    public Collection<String> whiteList = new ArrayList<String>();
    public int id;
    public String name;
    public boolean isPublic;
    public Account author;
    public Double price;
    public void calcPrice(){
        if(!this.getExpenses().isEmpty()){
            Expense e = this.getExpenses().iterator().next();
            price = e.getResultPrice();
        }
    }
    public static Project read() throws IOException{
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Expense.class, new ExpenseDeserializer());
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        DataInputStream in = App.in;
        String recievedStr = in.readUTF();
        System.out.println("RECIEVED "+recievedStr);
        Project res = gson.fromJson(recievedStr, Project.class);
        return res;
    }
    public void send() throws IOException{
        Gson gson = new Gson();
        String sendMe = gson.toJson(this);
        DataOutputStream out = App.out;
        System.out.println("SENT "+sendMe);
        out.writeUTF(sendMe);
    }
    public Collection<Expense> getExpenses() {
        return expenses;
    }
    public void setExpenses(Collection<Expense> expenses) {
        this.expenses = expenses;
    }
    public Collection<String> getWhiteList() {
        return whiteList;
    }
    public void setWhiteList(Collection<String> whiteList) {
        this.whiteList = whiteList;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isPublic() {
        return isPublic;
    }
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    public Account getAuthor() {
        return author;
    }
    public void setAuthor(Account author) {
        this.author = author;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
}
