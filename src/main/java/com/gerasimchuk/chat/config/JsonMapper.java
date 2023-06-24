/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.config;

public interface JsonMapper {
    String toJson(Object source);
    <T> T fromJson(String source, Class<T> clazz);
}
