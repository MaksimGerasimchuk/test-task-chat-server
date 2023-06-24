/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.ws;


import com.gerasimchuk.chat.config.WsGuiceInjectConfigurator;
import com.gerasimchuk.chat.service.api.ChatService;
import com.gerasimchuk.chat.service.api.in.MessageIn;
import com.google.inject.Inject;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


@ServerEndpoint(value = "/chat/{username}",
        decoders = WsMessageDecoder.class,
        encoders = WsMessageEncoder.class,
        configurator = WsGuiceInjectConfigurator.class)
public class ChatEndpoint {

    private static final Logger log = Logger.getLogger(ChatEndpoint.class.getName());
    // username -> session
    // be careful with multiple deploy
    private final Map<String, Set<Session>> userSessions = new ConcurrentHashMap<>();

    private ChatService chatService;

    @Inject
    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String userName) {
        var sessions = userSessions.get(userName);
        if (sessions == null) {
            sessions = new HashSet<>();
        }
        sessions.add(session);
        userSessions.put(userName, sessions);
        log.log(Level.INFO, () -> "Session opened:" + session);
    }

    @OnMessage
    public void onMessage(Session session, WsMessage message) throws IOException {
        log.log(Level.INFO, () -> "Message received for session " + session + ": " + message);
        var senderSessions = session.getOpenSessions();
        sendToOpenedSessions(senderSessions, message);
        var receiverSessions = userSessions.get(message.getNameTo());
        sendToOpenedSessions(receiverSessions, message);
        chatService.persistMessage(
                MessageIn.builder()
                        .nameFrom(message.getNameFrom())
                        .nameTo(message.getNameTo())
                        .content(message.getContent())
                        .dateTime(message.getDateTime())
                        .build()
        );
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String userName) {
        var sessions = userSessions.get(userName);
        if (sessions != null) {
            sessions.remove(session);
        }
        log.log(Level.INFO, () -> "Session closed: " + session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.log(Level.INFO, () -> "Error with session " + session + ": " + throwable.getMessage());
    }

    private void sendToOpenedSessions(Set<Session> sessions, WsMessage content) throws IOException {
        log.log(Level.INFO, () -> "Sending message to opened sessions " + sessions);
        if (sessions == null) {
            log.log(Level.INFO, () -> "Opened sessions not found");
            return;
        }
        for (Session s : sessions) {
            log.log(Level.INFO, () -> "Checking session: " + s);
            if (s.isOpen()) {
                try {
                    log.log(Level.INFO, () -> "Sesson " + s + " is open, sending message");
                    s.getBasicRemote().sendObject(content);
                } catch (EncodeException e) {
                    log.log(Level.INFO, () -> "Could not send message " + content + " to session " + s + ": " + e);
                }
                log.log(Level.INFO, () -> "Message sent");
            }
        }
    }

}
