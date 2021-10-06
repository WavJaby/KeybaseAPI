package com.wavjaby.bot.event;

import com.wavjaby.bot.DataSender;
import com.wavjaby.bot.KeybaseBot;
import com.wavjaby.bot.event.type.Event;
import com.wavjaby.json.JsonObject;

public class MessageEvent extends Event {
    private String messageText;


    public MessageEvent(JsonObject json, JsonObject content, KeybaseBot bot, DataSender dataSender) {
        super(json, bot, dataSender);
        messageText = content.getString("body");
//        if(content.notNull("userMentions")) {
//            content.getArray("userMentions");
//        }
    }

    public String getMessageText() {
        return messageText;
    }
}
