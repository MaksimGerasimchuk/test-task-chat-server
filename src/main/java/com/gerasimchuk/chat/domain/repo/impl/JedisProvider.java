/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.domain.repo.impl;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;

public final class JedisProvider {

    private static final Object lock = new Object();
    private static volatile boolean initialized = false;
    private static UnifiedJedis jedis;

    private JedisProvider() {
        throw new IllegalStateException("Not for instantiation!");
    }

    public static UnifiedJedis getInstance() {
        if (!initialized) {
            throw new IllegalStateException("Jedis instance is not initialized");
        }
        return jedis;
    }

    public static void init(String host, int port) {
        if (initialized) {
            throw new IllegalStateException("Jedis instance already initialized");
        }
        synchronized (lock) {
            jedis = new JedisPooled(host, port);
            initialized = true;
        }
    }
}
