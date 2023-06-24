/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.ws;

import com.gerasimchuk.chat.config.GsonMapperImpl;
import com.gerasimchuk.chat.config.JsonMapper;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class WsMessageDecoder implements Decoder.Text<WsMessage> {

    // no DI here because websocket server endpoint uses own instantiation mechanizm
    private static final JsonMapper gson = new GsonMapperImpl();

    @Override
    public WsMessage decode(String s) {
        return gson.fromJson(s, WsMessage.class);
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
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
