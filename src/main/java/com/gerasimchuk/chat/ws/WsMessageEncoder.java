/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.ws;

import com.google.gson.Gson;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class WsMessageEncoder implements Encoder.Text<WsMessage> {

    private static final Gson gson = new Gson();

    @Override
    public String encode(WsMessage object) {
        return gson.toJson(object);
    }

    @Override
    public void init(EndpointConfig config) {
        // do nothing here
    }

    @Override
    public void destroy() {
        // do nothing here
    }
}
