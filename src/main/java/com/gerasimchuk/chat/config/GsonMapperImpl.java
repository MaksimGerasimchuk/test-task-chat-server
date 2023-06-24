package com.gerasimchuk.chat.config;
/*
    © Герасимчук М. Ю. 2023
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;

@Singleton
public class GsonMapperImpl implements JsonMapper {

    private final Gson gson = new GsonBuilder().create();

    @Override
    public String toJson(Object source) {
        return gson.toJson(source);
    }

    @Override
    public <T> T fromJson(String source, Class<T> clazz) {
        return gson.fromJson(source, clazz);
    }
}
