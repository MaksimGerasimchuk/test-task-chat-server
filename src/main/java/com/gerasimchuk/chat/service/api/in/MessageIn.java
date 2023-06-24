/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.service.api.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageIn {
    private String nameFrom;
    private String nameTo;
    private String content;
    // here dateTime is String because of gson serialization/deserialization problems
    // which can not be solved via custom deserializers
    private String dateTime;

}
