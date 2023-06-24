/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.domain.document;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Message {
    private String chatId;
    private UUID messageId;
    private String nameFrom;
    private String nameTo;
    private String content;
    private String dateTime;
}
