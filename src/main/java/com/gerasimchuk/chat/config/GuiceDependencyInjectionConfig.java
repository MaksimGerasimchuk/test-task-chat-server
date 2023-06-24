/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.config;

import com.gerasimchuk.chat.domain.repo.api.ChatRepository;
import com.gerasimchuk.chat.domain.repo.api.UserRepository;
import com.gerasimchuk.chat.domain.repo.impl.ChatRepositoryImpl;
import com.gerasimchuk.chat.domain.repo.impl.UserRepositoryImpl;
import com.gerasimchuk.chat.rest.cors.CorsFilter;
import com.gerasimchuk.chat.rest.servlet.MessageHttpServlet;
import com.gerasimchuk.chat.rest.servlet.UserHttpServlet;
import com.gerasimchuk.chat.service.api.ChatService;
import com.gerasimchuk.chat.service.api.UserService;
import com.gerasimchuk.chat.service.impl.ChatServiceImpl;
import com.gerasimchuk.chat.service.impl.UserServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceDependencyInjectionConfig extends GuiceServletContextListener {

    static final Injector injector;

    static {
        injector = Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                super.configureServlets();
                filter("/*").through(CorsFilter.class);
                serve("/v1.0/users").with(UserHttpServlet.class);
                serve("/v1.0/messages").with(MessageHttpServlet.class);
                bind(UserService.class).to(UserServiceImpl.class);
                bind(UserRepository.class).to(UserRepositoryImpl.class);
                bind(ChatService.class).to(ChatServiceImpl.class);
                bind(ChatRepository.class).to(ChatRepositoryImpl.class);
                bind(JsonMapper.class).to(GsonMapperImpl.class);
            }
        });
    }

    @Override
    protected Injector getInjector() {
        return injector;
    }
}
