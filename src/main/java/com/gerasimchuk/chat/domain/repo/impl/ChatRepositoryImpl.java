/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.domain.repo.impl;

import com.gerasimchuk.chat.config.JsonMapper;
import com.gerasimchuk.chat.domain.document.Chat;
import com.gerasimchuk.chat.domain.document.Message;
import com.gerasimchuk.chat.domain.repo.api.ChatRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import redis.clients.jedis.UnifiedJedis;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class ChatRepositoryImpl implements ChatRepository {

    private static final Logger log = Logger.getLogger(ChatRepositoryImpl.class.getName());

    private JsonMapper jsonMapper;

    @Inject
    public void setJsonMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public Chat save(Message message) {
        log.log(Level.INFO, () -> "Saving message: " + message);
        UnifiedJedis jedis = JedisProvider.getInstance();
        var chatId = prepareChatId(message.getNameFrom(), message.getNameTo());
        message.setMessageId(UUID.randomUUID());
        jedis.lpush(chatId, jsonMapper.toJson(message));
        long count = jedis.llen(chatId);
        var saved = Chat.builder()
                .chatId(chatId)
                .participants(Set.of(message.getNameFrom(), message.getNameTo()))
                .messagesCount(count)
                .build();
        log.log(Level.INFO, () -> "Saved : " + saved);
        return saved;
    }

    @Override
    public List<Message> getAll(String p1, String p2) {
        log.log(Level.INFO, () -> "Getting all messages for participants " + p1 + " and " + p2);
        String chatId = prepareChatId(p1, p2);
        UnifiedJedis jedis = JedisProvider.getInstance();
        long count = jedis.llen(chatId);
        List<String> rawRes = jedis.lrange(chatId, 0, count - 1);
        var res = rawRes.stream().map(s -> jsonMapper.fromJson(s, Message.class)).toList();
        log.log(Level.INFO, () -> "Messages fetched successfully, count : " + res.size());
        return res;
    }

    private String prepareChatId(String p1, String p2) {
        int res = p1.compareTo(p2);
        if (res > 0) {
            return p1 + ":" + p2;
        } else {
            return p2 + ":" + p1;
        }
    }
}
