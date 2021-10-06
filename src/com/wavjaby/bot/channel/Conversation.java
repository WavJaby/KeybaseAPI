package com.wavjaby.bot.channel;

import com.wavjaby.bot.DataSender;
import com.wavjaby.bot.KeybaseBot;
import com.wavjaby.bot.queue.CommandCreateQueue;
import com.wavjaby.bot.queue.FileSendQueue;
import com.wavjaby.bot.queue.MessageSendQueue;
import com.wavjaby.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Conversation {
    private String id;
    private String topicType;
    private String name;
    private Channel channel;
    private boolean defaultConversation;
    private boolean unread;
    private long joinTimeSec;
    private long joinTime;
    private String memberStatus;

    private DataSender sender;
    //name, description
    private Map<String, String> commands = new HashMap<>();

    public Conversation(JsonObject json, JsonObject channelInfo, Channel channel, KeybaseBot bot, DataSender sender) {
        this.channel = channel;
        this.sender = sender;
        id = json.getString("id");
        topicType = channelInfo.getString("topic_type");
        name = channelInfo.getString("topic_name");
        defaultConversation = json.getBoolean("is_default_conv");
        unread = json.getBoolean("unread");
        joinTimeSec = json.getLong("active_at");
        joinTime = json.getLong("active_at_ms");
        memberStatus = json.getString("member_status");
    }

    public MessageSendQueue sendMessage(String message) {
        return new MessageSendQueue("{\"method\":\"send\",\"params\":{\"options\":{\"channel\":{\"name\":\"" + channel.getName() +
                "\",\"members_type\":\"" + channel.getMembersType() +
                "\",\"topic_name\":\"" + name + "\"},\"message\":{\"body\":\"" + message + "\"}}}}", sender);
    }

    public FileSendQueue sendFile(String description, String filePath) {
        return new FileSendQueue("{\"method\":\"attach\",\"params\":{\"options\":{\"channel\":{\"name\":\"" + channel.getName() +
                "\",\"members_type\":\"" + channel.getMembersType() +
                "\",\"topic_name\":\"" + name +
                "\"},\"filename\":\"" + filePath + "\",\"title\":\"" + description + "\"}}}", sender);
    }


    public void addCommand(String name, String description) {
        commands.put(name, description);
    }

    public CommandCreateQueue addCommands(String alias) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Map.Entry<String, String> i : commands.entrySet()) {
            builder.append("{\"name\":\"").append(i.getKey()).append("\", \"description\":\"").append(i.getValue()).append("\"},");
        }
        builder.setCharAt(builder.length() - 1, ']');

        return new CommandCreateQueue("{\"method\":\"advertisecommands\",\"params\":{\"options\":{\"alias\":\"" + alias +
                "\",\"advertisements\":[{\"type\":\"conv\",\"conv_id\":\"" + id +
                "\",\"commands\":" + builder + "}]}}}", sender);
    }

    public void clearCommand() {
        sender.send("{\"method\":\"clearcommands\",\"params\":{\"options\":{\"filter\":{\"type\":\"conv\",\"conv_id\":\"" + id + "\"}}}}", null);
        commands.clear();
    }

    public Map<String, String> commands() {
        return commands;
    }

    public String getID() {
        return id;
    }

    public String getTopicType() {
        return topicType;
    }

    public String getName() {
        return name;
    }
}
