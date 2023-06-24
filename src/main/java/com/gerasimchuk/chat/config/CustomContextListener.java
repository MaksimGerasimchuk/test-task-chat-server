/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.config;

import com.gerasimchuk.chat.domain.repo.impl.JedisProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class CustomContextListener implements ServletContextListener {

    private static final Logger log = Logger.getLogger(CustomContextListener.class.getName());

    private static final String REDIS_PROPERTY_NAME = "redis";
    private static final String REDIS_HOST_PROPERTY_NAME = "host";
    private static final String REDIS_PORT_PROPERTY_NAME = "port";

    @Override
    @SuppressWarnings("unchecked")
    public void contextInitialized(ServletContextEvent sce) {
        var props = ConfigProvider.getProps();
        log.log(Level.INFO, () -> "Properties bootstrapped: " + props);
        Object redisParams = props.get(REDIS_PROPERTY_NAME);
        String host = ((Map<String, String>) redisParams).get(REDIS_HOST_PROPERTY_NAME);
        log.log(Level.INFO, () -> "Host = " + host);
        Integer port = (Integer) ((Map) redisParams).get(REDIS_PORT_PROPERTY_NAME);
        log.log(Level.INFO, () -> "Port = " + port);
        JedisProvider.init(host, port);
        var jedis = JedisProvider.getInstance();
        log.log(Level.INFO, () -> "Jedis instance obtained: " + jedis);
    }
}
