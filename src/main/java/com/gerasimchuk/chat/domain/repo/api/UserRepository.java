/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.domain.repo.api;

import com.gerasimchuk.chat.domain.document.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> getByName(String name);
    List<User> getAll();
}
