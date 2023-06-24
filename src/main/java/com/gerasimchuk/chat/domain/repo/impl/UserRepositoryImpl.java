/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.domain.repo.impl;

import com.gerasimchuk.chat.config.JsonMapper;
import com.gerasimchuk.chat.domain.document.User;
import com.gerasimchuk.chat.domain.repo.api.UserRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import redis.clients.jedis.UnifiedJedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class UserRepositoryImpl implements UserRepository {

    private static final Logger log = Logger.getLogger(UserRepositoryImpl.class.getName());

    private static final String USERS_HASH_PREFIX = "users#";
    private static final String USERS_COLLECTION_NAME = "users";
    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";

    private JsonMapper jsonMapper;

    @Inject
    public void setJsonMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public User save(User user) {
        log.log(Level.INFO, () -> "Saving user: " + user);
        UnifiedJedis jedis = JedisProvider.getInstance();
        String userKey = prepareUserKey(user.getName());
        Map<String, String> existingHash = jedis.hgetAll(userKey);
        if (existingHash == null || existingHash.isEmpty()) {
            log.log(Level.INFO, () -> "Existing user not found, creating new user");
            var userId = UUID.randomUUID();
            user.setId(userId);
            Map<String, String> hash = new HashMap<>();
            hash.put(ID_KEY, userId.toString());
            hash.put(NAME_KEY, user.getName());
            jedis.hset(userKey, hash);
            jedis.rpush(USERS_COLLECTION_NAME, jsonMapper.toJson(user));
            var result = User.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .build();
            log.log(Level.INFO, () -> "Created: " + result);
            return result;
        } else {
            String id = existingHash.get(ID_KEY);
            String name = existingHash.get(NAME_KEY);
            var existing = User.builder()
                    .id(UUID.fromString(id))
                    .name(name)
                    .build();
            log.log(Level.INFO, () -> "Existing user found:" + existing);
            return existing;
        }
    }

    @Override
    public Optional<User> getByName(String name) {
        log.log(Level.INFO, () -> "Fetching user by name" + name);
        UnifiedJedis jedis = JedisProvider.getInstance();
        Map<String, String> existingHash = jedis.hgetAll(prepareUserKey(name));
        if (existingHash == null || existingHash.isEmpty()) {
            log.log(Level.INFO, () -> "User not found");
            return Optional.empty();
        } else {
            String id = existingHash.get(ID_KEY);
            String username = existingHash.get(NAME_KEY);
            var found = Optional.of(User.builder()
                    .id(UUID.fromString(id))
                    .name(username)
                    .build());
            log.log(Level.INFO, () -> "Found: " + found);
            return found;
        }
    }

    @Override
    public List<User> getAll() {
        log.log(Level.INFO, () -> "Fetching all users");
        UnifiedJedis jedis = JedisProvider.getInstance();
        var count = jedis.llen(USERS_COLLECTION_NAME);
        log.log(Level.INFO, () -> "Users count: " + count);
        return jedis.lrange(USERS_COLLECTION_NAME, 0, count - 1).stream()
                .map(s -> jsonMapper.fromJson(s, User.class))
                .toList();
    }

    private String prepareUserKey(String name) {
        return USERS_HASH_PREFIX + name;
    }

}
