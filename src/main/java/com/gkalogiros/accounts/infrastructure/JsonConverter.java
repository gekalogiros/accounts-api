package com.gkalogiros.accounts.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonConverter
{
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new DateTimeDeserializer())
            .setPrettyPrinting()
            .create();

    public <T> T fromJson(final String json, Class<T> clazz)
    {
        return GSON.fromJson(json, clazz);
    }

    public String toJson(final Object object)
    {
        return GSON.toJson(object);
    }

    private static class DateTimeDeserializer implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate>
    {
        @Override
        public LocalDate deserialize(final JsonElement json,
                                     final Type typeOfT,
                                     final JsonDeserializationContext context)
        {
            return LocalDate.parse(json.getAsString(), DateTimeFormatter.BASIC_ISO_DATE);
        }

        @Override
        public JsonElement serialize(final LocalDate localDate,
                                     final Type type,
                                     final JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(localDate == null ? "" : DateTimeFormatter.BASIC_ISO_DATE.format(localDate));
        }
    }
}
