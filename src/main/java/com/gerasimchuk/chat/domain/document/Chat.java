/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.domain.document;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Chat {
    private String chatId;
    private Set<String> participants;
    private long messagesCount;
}
