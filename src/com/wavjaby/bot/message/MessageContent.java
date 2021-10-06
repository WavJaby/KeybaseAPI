package com.wavjaby.bot.message;

import com.wavjaby.json.JsonObject;

public class MessageContent {
    private MessageType type;
    private String text;

    public MessageContent(JsonObject json) {
        type = MessageType.valueOf(json.getString("type"));
        switch (type) {
            case text:
                text = json.getJson("text").getString("body");
                break;
            default:
                System.err.println("[" + getClass().getName() + "] New Type: " + json.getString("type"));
        }
    }

    public MessageType getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
