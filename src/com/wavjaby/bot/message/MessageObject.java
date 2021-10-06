package com.wavjaby.bot.message;

import com.wavjaby.bot.DataSender;
import com.wavjaby.bot.channel.Channel;
import com.wavjaby.bot.user.Sender;
import com.wavjaby.json.JsonObject;

public class MessageObject {
    private long messageID;
    private MessageContent messageContent;

    public MessageObject(JsonObject json, DataSender dataSender){
        messageID = json.getInt("id");
        messageContent = new MessageContent(json.getJson("content"));
    }

    public long getMessageID() {
        return messageID;
    }

    public MessageContent getContent() {
        return messageContent;
    }
}
