package com.wavjaby.bot.channel;

import com.wavjaby.bot.DataSender;
import com.wavjaby.json.JsonObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Channel {
    private String name;
    private MemberType membersType;

    //id, Conversation
    private Map<String, Conversation> conversations = new HashMap();
    private DataSender dataSender;

    public Channel(JsonObject json, DataSender dataSender) {
        name = json.getString("name");
        membersType = MemberType.valueOf(json.getString("members_type"));

        this.dataSender = dataSender;
    }

    public Conversation getConversationByName(String name) {
        for (Conversation conversation : conversations.values()){
            if(conversation.getName().equals(name))
                return conversation;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public MemberType getMembersType() {
        return membersType;
    }

    public void addConversation(Conversation conversation) {
        conversations.put(conversation.getID(), conversation);
    }

    public Conversation getConversation(String conversationID) {
        return conversations.get(conversationID);
    }

    public Collection<Conversation> getConversations() {
        return conversations.values();
    }
}
