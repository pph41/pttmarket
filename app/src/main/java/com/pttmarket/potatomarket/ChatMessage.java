package com.pttmarket.potatomarket;


public class ChatMessage {
    private String messageText;
    private String senderName;
    private String imageUrl;

    public ChatMessage() {
        // Firebase에서 필요한 기본 생성자
    }

    public ChatMessage(String messageText, String senderName) {
        this.messageText = messageText;
        this.senderName = senderName;
    }

    public ChatMessage(String messageText, String senderName, String imageUrl) {
        this.messageText = messageText;
        this.senderName = senderName;
        this.imageUrl = imageUrl;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
