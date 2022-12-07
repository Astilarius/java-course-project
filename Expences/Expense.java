package Expences;

// в окне можно будет добавлять расходы
// расходы будут транспортные, на материалы и на производство
// каждый расход будет отображаться на экране
// при создании любого расхода нужно будет указать сумму
// каждый расход можно редактировать
// каждый расход можно удалять
// потом вычисляется общая сумма
// у всех расходов должен быть метод возвращающий общую сумму расхода
// у всех расходов должен быть метод для изменения значений
import javafx.scene.layout.VBox;
import java.io.DataOutputStream;
import java.util.Collection;
import java.util.ArrayList;

public interface Expense {
    
    public static Collection<Expense> expenses = new ArrayList<Expense>();
    public VBox getElem();
    public void sendToServer(DataOutputStream out);
    public String getString();
    public String getId(); 
    public String getName(); 
    // public VBox edit(VBox elem);
}