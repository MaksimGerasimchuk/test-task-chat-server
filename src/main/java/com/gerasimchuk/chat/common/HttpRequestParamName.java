/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.common;

public enum HttpRequestParamName {
    USER_FROM("userFrom"),
    USER_TO("userTo"),
    EXPORT("export");

    String paramName;

    HttpRequestParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
