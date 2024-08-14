package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.example.parse.Field;
import org.example.parse.FieldType;
import org.example.util.JsonHelper;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ValidateMsg {
    Set<String> extra;  // 多出来的
    Set<String> missing; // 缺失的
    Set<String> illega_enum; // 枚举值不合法
    Set<String> required; // 空值
    Set<String> not_composite; // 不是组合类型


    // 获取Protocol的字段数组
    public static List<Field> getFields(JsonElement protocol) {
        Type type = new TypeToken<List<Field>>() {
        }.getType();
        JsonArray jsonArray = protocol.getAsJsonObject().get("field").getAsJsonArray();
        return new Gson().fromJson(jsonArray, type);
    }

    // 获取枚举的合法值（效率低）
    public static Set<Integer> getLegalEnumValueByName(JsonElement meta_json_ele, String name){
        Set<Integer> legal_values = new HashSet<>();
        meta_json_ele.getAsJsonObject().get("enumType").getAsJsonArray().forEach(enum_ele -> {
            if(enum_ele.getAsJsonObject().get("name").getAsString().equals(name)){
                enum_ele.getAsJsonObject().get("value").getAsJsonArray().forEach(value_ele -> {
                    legal_values.add(value_ele.getAsJsonObject().get("number").getAsInt());
                });
            }
        });
        return legal_values;
    }

    public static String getSuffix(String name){
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ""; // No extension found
        }
        return name.substring(lastDotIndex + 1);
    }


    public static ValidateMsg validate(String meta, String data) throws Exception {
        JsonElement data_json_ele = JsonParser.parseString(data);
        JsonElement meta_json_ele = JsonParser.parseString(meta);

        // 获取json
        JsonElement protocol = meta_json_ele.getAsJsonObject()
                .get("messageType")
                .getAsJsonArray().get(0);

        if (!JsonHelper.readStringFromObject(protocol, "name").equals("Protocol")) {
            throw new Exception("not a protocol");
        }

        // data中的所有key
        Set<String> data_keys = new TreeSet<>();
        data_json_ele.getAsJsonObject().entrySet().forEach(entry -> {
            data_keys.add(entry.getKey());
        });

        List<Field> fields = getFields(protocol);
        // protocol中的所有key
        Set<String> protocol_keys = new TreeSet<>();
        fields.forEach(field -> {
            protocol_keys.add(field.getName());
        });

        // 保存校验结果
        ValidateMsg msg = new ValidateMsg();
        // 1.1 多余的
        msg.extra = data_keys.stream().filter(key -> !protocol_keys.contains(key)).collect(Collectors.toSet());

        // 1.2 缺失的
        msg.missing = protocol_keys.stream().filter(key -> !data_keys.contains(key)).collect(Collectors.toSet());

        // 获取两边都存在的键
        Set<String> both_keys = data_keys.stream().filter(key -> !protocol_keys.contains(key)).collect(Collectors.toSet());

        // 2.枚举值不合法
        // 过滤所有枚举字段
        msg.illega_enum = fields.stream().filter(field -> {
            if(field.getType().equals(FieldType.ENUM)){
                // 获取枚举的合法类型
                Set<Integer> legal_values = getLegalEnumValueByName(meta_json_ele, getSuffix(field.getTypeName()));
                return !legal_values.contains(Integer.parseInt(JsonHelper.readStringFromObject(data_json_ele, field.getName())));
            }
            return false;
        }).map(Field::getName).collect(Collectors.toSet());

        // 3. 空值
        msg.required = fields.stream().filter(field -> {
            if(field.getLabel().equals("LABEL_REQUIRED")) {
                if(!JsonHelper.isNull(data_json_ele, field.getName())){
                    return JsonHelper.readStringFromObject(data_json_ele, field.getName()).isEmpty();
                }
            }
            return false;
        }).map(Field::getName).collect(Collectors.toSet());

        // 4.不是组合类型
        msg.not_composite = fields.stream().filter(field -> {
            if(field.getType().equals(FieldType.MESSAGE)){
                return JsonHelper.isJsonObject(data_json_ele, field.getName());
            }
            return false;
        }).map(Field::getName).collect(Collectors.toSet());

        return msg;
    }

    @Override
    public String toString(){
        return "extra: " + extra + "\n" +
                "missing: " + missing + "\n" +
                "illega_enum: " + illega_enum + "\n" +
                "required: " + required + "\n" +
                "not_composite: " + not_composite + "\n";
    }
}
