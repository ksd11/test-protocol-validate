import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

public class JsonTest {

    private String name;
    private int age;

    @Test
    public void test(){
        String json = "{\"key1\":\"value1\", \"key2\":{\"subKey1\":\"subValue1\"}}";

        Gson gson = new Gson();
        JsonElement jelement = JsonParser.parseString(json);

        // 直接查找顶层键
        JsonElement valueForKey1 = jelement.getAsJsonObject().get("key1");
        System.out.println("Value for key1: " + valueForKey1.getAsString());

        System.out.println(jelement.getAsJsonObject().get("key2").isJsonObject());
        // 查找嵌套键
        JsonElement valueForSubKey1 = jelement.getAsJsonObject().get("key2").getAsJsonObject().get("subKey1");
        System.out.println("Value for subKey1: " + valueForSubKey1.getAsString());

        // 遍历所有键值对
        jelement.getAsJsonObject().entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });
    }
}
