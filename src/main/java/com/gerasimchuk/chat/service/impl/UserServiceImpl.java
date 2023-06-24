/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.service.impl;

import com.gerasimchuk.chat.domain.document.User;
import com.gerasimchuk.chat.domain.repo.api.UserRepository;
import com.gerasimchuk.chat.exception.AlreadyRegisteredException;
import com.gerasimchuk.chat.service.api.UserService;
import com.gerasimchuk.chat.service.api.out.UserOut;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class UserServiceImpl implements UserService {

    private static final Logger log = Logger.getLogger(UserServiceImpl.class.getName());

    private UserRepository userRepository;

    @Inject
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserOut register(String name) {
        log.log(Level.INFO, () -> "Registering user with name " + name);
        var mayBeUser = userRepository.getByName(name);
        if (mayBeUser.isPresent()) {
            log.log(Level.INFO, () -> "User with name " + name + " already exists");
            throw new AlreadyRegisteredException(name);
        }
        log.log(Level.INFO, () -> "User with name " + name + " not found, creating new");
        var saved = userRepository.save(User.builder()
                .id(null)
                .name(name)
                .build());
        log.log(Level.INFO, () -> "New user created: " + saved);
        return new UserOut(saved.getId(), saved.getName());
    }

    @Override
    public List<UserOut> getAll() {
        log.log(Level.INFO, () -> "Fetching all users");
        var result = userRepository.getAll().stream()
                .map(u -> new UserOut(u.getId(), u.getName()))
                .toList();
        log.log(Level.INFO, () -> "Users fetched, count: " + result.size());
        return result;
    }
}
