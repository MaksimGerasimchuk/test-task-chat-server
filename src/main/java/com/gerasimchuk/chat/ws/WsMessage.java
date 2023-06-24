/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.ws;

import lombok.Data;

@Data
public class WsMessage {
    private String nameFrom;
    private String nameTo;
    private String content;
    private String dateTime;
}
