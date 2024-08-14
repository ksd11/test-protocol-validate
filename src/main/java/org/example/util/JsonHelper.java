package org.example.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonHelper {
    public static String readStringFromObject(JsonElement element, String key){
        return element.getAsJsonObject().get(key).getAsString();
    }

    public static int readIntFromObject(JsonElement element, String key){
        return element.getAsJsonObject().get(key).getAsInt();
    }

    public static JsonArray readJsonArrayFromObject(JsonElement element, String key){
        return element.getAsJsonObject().get(key).getAsJsonArray();
    }

    public static boolean isJsonObject(JsonElement element, String key){
        return element.getAsJsonObject().get(key).isJsonObject();
    }

    public static boolean isNull(JsonElement element, String key){
        return element.getAsJsonObject().get(key).isJsonNull();
    }


}
