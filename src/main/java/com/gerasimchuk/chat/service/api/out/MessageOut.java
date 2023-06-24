package com.gerasimchuk.chat.service.api.out;

import java.util.UUID;


public record MessageOut(UUID messageId, String nameFrom, String to, String content, String dateTime) {
}
