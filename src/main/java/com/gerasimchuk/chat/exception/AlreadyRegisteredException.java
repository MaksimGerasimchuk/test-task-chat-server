/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.exception;

public class AlreadyRegisteredException extends RuntimeException{
    public AlreadyRegisteredException(String userName) {
        super("User " + userName + " already registered");
    }
}
