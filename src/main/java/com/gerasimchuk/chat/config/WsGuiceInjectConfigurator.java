/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.config;

import javax.websocket.server.ServerEndpointConfig;

public class WsGuiceInjectConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) {
        return GuiceDependencyInjectionConfig.injector.getInstance(endpointClass);
    }
}
