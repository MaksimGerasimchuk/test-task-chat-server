/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.domain.document;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class User {
    private UUID id;
    private String name;
}
