package java_course_project_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

import com.google.gson.Gson;

import Models.Account;
import Models.Project;

class ClientHandler implements Runnable {
    // private final Socket clientSocket;

    // Constructor
    public ClientHandler(Socket socket)
    {
        App.sock = socket;
    }

    public void run()
    {
        try {
            App.in = new DataInputStream(App.sock.getInputStream());
            App.out = new DataOutputStream(App.sock.getOutputStream());
            Account a = new Account();
            Gson gson = new Gson();
            String line;
            a.setVerified(false);

            while(!a.isVerified()){
                line = App.in.readUTF();
                System.out.println(line);
                a = gson.fromJson(line, Account.class);
                if(a.isVerified()){
                    a = DatabaseController.getAccountByName(a.getAccountName());
                    if(a.isVerified()){
                        line = gson.toJson(a);
                        System.out.println(line);
                        App.out.writeUTF(line);
                        a = Account.read();
                        continue;
                    } else {
                        a = new Account();
                        a.setAccountName("Неверный пароль или имя аккаунта");
                        line = gson.toJson(a);
                        a.setVerified(false);
                        System.out.println(line);
                        App.out.writeUTF(line);
                    }
                } else {
                    if(DatabaseController.writeAccount(a)){
                        // PrimaryController.showAccounts();
                        App.out.writeUTF(line);
                    } else {
                        a = new Account();
                        a.setAccountName("Аккаунт с таким именем уже существует");
                        a.setVerified(false);
                        line = gson.toJson(a);
                        App.out.writeUTF(line);
                    }
                }
            }
            while(true){
                Project temp = Project.read();
                
                if (temp.getId() == 0){
                    DatabaseController.writeProject(temp);
                    temp = DatabaseController.getLastProject();
                    temp.send();
                } else if (temp.getId() == -1){
                    String authorName = temp.getAuthor().getAccountName();
                    Collection<Project> projects = DatabaseController.getPublicProjects(authorName);
                    for (Project p : projects){
                        p.send();
                    }
                    projects.clear();
                    projects = DatabaseController.getProjectsByAuthor(authorName);
                    for (Project p : projects){
                        p.send();
                    }
                    projects.clear();
                    projects = DatabaseController.getWhitelistedProjects(authorName);
                    for (Project p : projects){
                        p.send();
                    }
                    temp.send();
                }else if (temp.getId() < -1){
                    DatabaseController.deleteProject(temp);
                } else {
                    temp.calcPrice();
                    DatabaseController.updateProject(temp);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
        }
        
    }
}