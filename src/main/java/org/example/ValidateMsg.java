package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.List;

public class ValidateMsg {
    List<String> extra;  // 多出来的
    List<String> missing; // 缺失的
    List<String> illega_enum; // 枚举值不合法
    List<String> required; // 空值
    List<String> not_composite; // 不是组合类型


    public String getField(JsonElement protocol, String name){

    }


    public ValidateMsg validate(String meta, String data) throws Exception {
        Gson data_json = new Gson();
        JsonElement data_json_ele = JsonParser.parseString(data);

        Gson meta_gson = new Gson();
        JsonElement meta_json_ele = JsonParser.parseString(meta);
        JsonElement protocol = meta_json_ele.getAsJsonObject()
                .get("messageType")
                .getAsJsonArray().get(0);

        if(protocol.getAsJsonObject().get("name").getAsString().equals("Protocol")){
            throw new Exception("not a protocol");
        }

        data_json_ele.getAsJsonObject().entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });

    }
}
