package com.pttmarket.potatomarket;

public class ChatMessage {
    private String messageId;
    private String senderId;
    private String roomName;
    private String message;
    private long timestamp;
    private String imageUrl;
    private String senderNickname;

    // 기본 생성자 (Firebase에서 데이터를 읽어올 때 필요)
    public ChatMessage() {
    }

    // 메시지 보낼 때 사용하는 생성자
    public ChatMessage(String messageId, String senderId, String roomName, String message, long timestamp, String imageUrl) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.roomName = roomName;
        this.message = message;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;

        int idx = senderId.indexOf("@");
        this.senderNickname = idx != -1 ? senderId.substring(0, idx) : senderId;
    }

    // Getter 및 Setter 메서드
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }


}
