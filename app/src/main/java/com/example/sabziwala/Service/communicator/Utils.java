package com.example.sabziwala.Service.communicator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

public class Utils {
    public static boolean isBlankOrNull(String s) {
        return s == null || s.trim().equals("");
    }

    public static Gson getGson() {
        JsonSerializer<Date> ser = new JsonSerializer<Date>() {
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                return src == null ? null : new JsonPrimitive(src.getTime());
            }
        };
        JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    return json == null ? null : ISO8601.getDateInUTC(json.getAsString());
                }
                catch (ParseException e) {
                    return null;
                }
            }
        };

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, ser)
                .registerTypeAdapter(Date.class, deser)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson;
    }
}
