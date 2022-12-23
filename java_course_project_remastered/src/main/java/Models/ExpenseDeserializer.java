package Models;

import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

public class ExpenseDeserializer implements JsonDeserializer<Expense> {
    @Override
    public Expense deserialize(JsonElement json, Type member, JsonDeserializationContext context) {
        int myType = json.getAsJsonObject().get("type").getAsInt();
        switch (myType) {
            case 1: return context.deserialize(json, Material.class);
            case 2: return context.deserialize(json, Production.class);
            default: return null;
        }
    }
}