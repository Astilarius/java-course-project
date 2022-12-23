package Models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.Gson;

import java_course_project_server.App;

public class Account{
    public Integer id;
    public String accountName;
    public String hash;
    public String salt;
    public boolean verified;
    public static Account read() throws IOException{
        DataInputStream in = App.in;
        String recievedStr = in.readUTF();
        System.out.println(recievedStr);
        Gson gson = new Gson();
        Account res = gson.fromJson(recievedStr, Account.class);
        return res;
    }
    public void send() throws IOException{
        Gson gson = new Gson();
        String sendMe = gson.toJson(this);
        DataOutputStream out = App.out;
        System.out.println(sendMe);
        out.writeUTF(sendMe);
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public boolean isVerified() {
        return verified;
    }
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}