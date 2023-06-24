/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.service.impl;

import com.gerasimchuk.chat.domain.document.Message;
import com.gerasimchuk.chat.domain.repo.api.ChatRepository;
import com.gerasimchuk.chat.service.api.ChatService;
import com.gerasimchuk.chat.service.api.in.MessageIn;
import com.gerasimchuk.chat.service.api.out.MessageOut;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
public class ChatServiceImpl implements ChatService {

    private static final Logger log = Logger.getLogger(ChatServiceImpl.class.getName());

    private ChatRepository chatRepository;

    @Inject
    public void setChatRepository(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void persistMessage(MessageIn message) {
        log.log(Level.INFO, () -> "Persisting message " + message);
        var persisted = chatRepository.save(Message.builder()
                .chatId(null)
                .messageId(null)
                .nameFrom(message.getNameFrom())
                .nameTo(message.getNameTo())
                .content(message.getContent())
                .dateTime(message.getDateTime())
                .build());
        log.log(Level.INFO, () -> "Persisted: " + persisted);
    }

    @Override
    public List<MessageOut> getHistory(String p1, String p2) {
        log.log(Level.INFO, () -> "Fetching history for participants " + p1 + " and " + p2);
        var history = chatRepository.getAll(p1, p2).stream().map(m -> new MessageOut(
                m.getMessageId(),
                m.getNameFrom(),
                m.getNameTo(),
                m.getContent(),
                m.getDateTime()
        )).collect(Collectors.toCollection(ArrayList::new));
        log.log(Level.INFO, () -> "History found, messages count:" + history.size());
        Collections.reverse(history);
        return history;
    }
}
