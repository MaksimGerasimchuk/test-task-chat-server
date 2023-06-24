/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.rest.servlet;

import com.gerasimchuk.chat.exception.AlreadyRegisteredException;
import com.gerasimchuk.chat.rest.request.UserCreateRequest;
import com.gerasimchuk.chat.service.api.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "userServlet", urlPatterns = "/v1.0/users")
@Singleton
public class UserHttpServlet extends AbstractHttpServlet {

    private static final Logger log = Logger.getLogger(UserHttpServlet.class.getName());


    private UserService userService;

    // setter injection to avoid 'java.lang.NoSuchMethodException: <init>()'
    // due to an attempt to call non-existing constructor without params
    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        log.log(Level.INFO, () -> "Preforming GET request for fetching user");
        var allUsers = userService.getAll();
        writeAndFlush(resp, allUsers);
        log.log(Level.INFO, () -> "GET request for fetching user finished successfully");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        log.log(Level.INFO, () -> "Preforming POST request for user creation");
        var userRequest = getBody(req, UserCreateRequest.class);
        log.log(Level.INFO, () -> "Request body: " + userRequest);
        if (userRequest == null) {
            log.log(Level.INFO, () -> "Found null request body, writing error");
            writeError(resp, "Expected request body");
            return;
        }
        try {
            var registered = userService.register(userRequest.getName());
            log.log(Level.INFO, () -> "User registered: " + registered);
            writeAndFlush(resp, registered);
            log.log(Level.INFO, () -> "POST request for user creation finished successfully");
        } catch (AlreadyRegisteredException e) {
            log.log(Level.INFO, () -> "User already registered, writing error ...");
            writeError(resp, "User already registered");
        }
    }

}
