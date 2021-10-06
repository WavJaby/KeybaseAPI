package com.wavjaby.bot.event.type;

import com.wavjaby.bot.DataSender;
import com.wavjaby.bot.KeybaseBot;
import com.wavjaby.bot.channel.Channel;
import com.wavjaby.bot.channel.Conversation;
import com.wavjaby.bot.user.SelfUser;
import com.wavjaby.bot.user.Sender;
import com.wavjaby.bot.user.User;
import com.wavjaby.json.JsonObject;

public class Event {
    private String conversationID;
    private Conversation conversation;
    private Channel channel;
    private Sender sender;
    private long timeStampSec;
    private long timeStamp;

    private SelfUser selfUser;

    public Event(JsonObject json, KeybaseBot bot, DataSender dataSender) {
        conversationID = json.getString("conversation_id");
        channel = bot.getChannel(json.getJson("channel"));
        sender = new Sender(json.getJson("sender"), dataSender);
        timeStampSec = json.getLong("sent_at");
        timeStamp = json.getLong("sent_at_ms");
        conversation = channel.getConversation(conversationID);

        selfUser = bot.getSelfUser();
    }

    public Event(Conversation conversation, Channel channel, Sender sender, long timeStampSec, long timeStamp) {
        this.conversationID = conversation.getID();
        this.conversation = conversation;
        this.channel = channel;
        this.sender = sender;
        this.timeStampSec = timeStampSec;
        this.timeStamp = timeStamp;
    }

    public SelfUser getSelfUser(){
        return selfUser;
    }

    public String getConversationID() {
        return conversationID;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public Channel getChannel() {
        return channel;
    }

    public Sender getSender() {
        return sender;
    }

    public long getTimeStampSec() {
        return timeStampSec;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
