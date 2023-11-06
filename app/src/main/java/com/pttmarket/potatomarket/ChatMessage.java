package com.pttmarket.potatomarket;

public class ChatMessage {
    private String message;
    private String username;

    public ChatMessage() {
        // Default constructor required for Firebase
    }

    public ChatMessage(String message, String username) {
        this.message = message;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }
}
