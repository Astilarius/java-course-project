package Models;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public interface Expense {
    public double getResultPrice();
    public void write(FlowPane fp);
    public int getType();
    public static Collection<Expense> expenses = new ArrayList<Expense>();
    public VBox getElem();
    public void sendToServer(DataOutputStream out);
    public String getString();
    public String getId(); 
    public String getName(); 
    // public VBox edit(VBox elem);
}