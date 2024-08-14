package org.example.util;

import com.google.gson.JsonElement;

public class JsonHelper {
    public static String readStringFromObject(JsonElement element, String key){
        return element.getAsJsonObject().get(key).getAsString();
    }

    public static boolean isJsonObject(JsonElement element, String key){
        return element.getAsJsonObject().get(key).isJsonObject();
    }

    public static boolean isNull(JsonElement element, String key){
        return element.getAsJsonObject().get(key).isJsonNull();
    }
}
