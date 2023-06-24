/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.service.api;

import com.gerasimchuk.chat.service.api.in.MessageIn;
import com.gerasimchuk.chat.service.api.out.MessageOut;

import java.util.List;

public interface ChatService {
    void persistMessage(MessageIn message);
    List<MessageOut> getHistory(String participant1, String participant2);
}
