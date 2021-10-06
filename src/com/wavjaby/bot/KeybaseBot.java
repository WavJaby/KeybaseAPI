package com.wavjaby.bot;

import com.wavjaby.bot.channel.Channel;
import com.wavjaby.bot.channel.Conversation;
import com.wavjaby.bot.event.ChannelEventListener;
import com.wavjaby.bot.event.KeybaseEvent;
import com.wavjaby.bot.user.SelfUser;
import com.wavjaby.bot.user.User;
import com.wavjaby.json.JsonObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KeybaseBot {
    private final String TAG = "[KeybaseBot] ";
    private final String keybaseHome;
    private final String botHome;

    private ChannelEventListener listener;
    private DataSender sender;

    private SelfUser self;
    private Map<String, Channel> channels = new HashMap<>();
    private Map<String, User> users = new HashMap<>();

    public KeybaseBot(String keybaseHome, String botHome) {
        this.keybaseHome = keybaseHome;
        this.botHome = "--home=" + botHome;
        listener = new ChannelEventListener(this);
    }

    public void start() {
        sender = new DataSender(keybaseHome, botHome, "chat", "api");
        listener.setSender(sender);
        listener.start(keybaseHome, botHome, "chat", "api-listen");
        self = new SelfUser(new JsonObject(
                sender.sendCommandCLI(keybaseHome, botHome, "whoami", "-j")
        ), sender);
        for (Object i : new JsonObject(sender.send("{\"method\": \"list\"}")).getJson("result").getArray("conversations")) {
            JsonObject data = (JsonObject) i;
            JsonObject channelInfo = data.getJson("channel");
            Channel channel;
            if ((channel = channels.get(channelInfo.getString("name"))) == null) {
                channel = new Channel(channelInfo, sender);
                channels.put(channel.getName(), channel);
            }
            channel.addConversation(new Conversation(data, channelInfo, channel, this, sender));
        }
        channels.values().forEach(channel -> listener.channelReady(channel));
        listener.botReady(this);
        System.out.println(TAG + "Bot started");
    }

    public void stop() {
        listener.stop();
        sender.stop();
    }


    public Channel getChannel(JsonObject channelData) {
        Channel channel;
        if ((channel = channels.get(channelData.getString("name"))) == null) {
            channel = new Channel(channelData, sender);
            channels.put(channel.getName(), channel);
        }
        return channel;
    }

    public Channel getChannelByName(String name) {
        return channels.get(name);
    }

    public Collection<Channel> getChannels() {
        return channels.values();
    }

    public SelfUser getSelfUser() {
        return self;
    }

    public void addEventListener(KeybaseEvent listener) {
        this.listener.addListener(listener);
    }
}
