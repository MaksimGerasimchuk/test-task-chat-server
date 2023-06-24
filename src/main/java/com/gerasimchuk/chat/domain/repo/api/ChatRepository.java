/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.domain.repo.api;

import com.gerasimchuk.chat.domain.document.Chat;
import com.gerasimchuk.chat.domain.document.Message;

import java.util.List;

public interface ChatRepository {
    Chat save(Message message);

    // in real world scenario it's better to implement pagination
    List<Message> getAll(String participant1, String participant2);
}
