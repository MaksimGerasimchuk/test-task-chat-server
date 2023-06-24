/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.service.api;

import com.gerasimchuk.chat.service.api.out.UserOut;

import java.util.List;

public interface UserService {
    UserOut register(String name);

    List<UserOut> getAll();
}
