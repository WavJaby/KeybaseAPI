package com.wavjaby.bot.event;

import com.wavjaby.bot.DataSender;
import com.wavjaby.bot.KeybaseBot;
import com.wavjaby.bot.channel.Channel;
import com.wavjaby.bot.event.type.SystemType;
import com.wavjaby.bot.message.MessageType;
import com.wavjaby.json.JsonObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

public class ChannelEventListener {
    private final String TAG = "[ChannelEventListener] ";
    private DataSender sender;
    private KeybaseBot bot;

    public ChannelEventListener(KeybaseBot bot) {
        this.bot = bot;
    }

    private InputStream in;
    private BufferedReader reader;
    private boolean start;

    public void start(final String... commands) {
        new Thread(() -> {
            try {
                start = true;
                Process proc = new ProcessBuilder(commands).start();
                in = proc.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                System.out.println(TAG + "Listener start");
                while (start) {
                    String line = reader.readLine();
                    if (line == null || line.charAt(0) == '-') {
                        System.out.println("w" + line);
                    } else {
                        JsonObject data = new JsonObject(line);
                        onEvent(data);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(TAG + "Listener stop");
        }).start();
    }

    public void stop() {
        start = false;
    }

    private void onEvent(JsonObject data) {
//        System.out.println(data.toStringBeauty());
        EventType type = EventType.valueOf(data.getString("type"));
//        String source = data.getString("source");
        switch (type) {
            case chat:
                JsonObject messageData = data.getJson("msg");
                JsonObject content = messageData.getJson("content");
                switch (MessageType.valueOf(content.getString("type"))) {
                    case system:
                        JsonObject system = content.getJson("system");
                        switch (SystemType.valueOf(system.getInt("systemType"))) {
                            case ADDED_TO_TEAM:
                                JoinTeamEvent joinTeamEvent = new JoinTeamEvent(messageData, system.getJson("addedtoteam"), bot, sender);
                                forEach(i -> i.onJoinTeam(joinTeamEvent));
                            default:
                                System.err.println("[" + getClass().getName() + "] New system type: " + data.getString("type"));
                        }
                        break;
                    case text:
//                        System.out.println(messageData);
                        MessageEvent event = new MessageEvent(messageData, content.getJson("text"), bot, sender);
                        String message = event.getMessageText();
                        if (message.charAt(0) == '!') {
                            if (event.getConversation().commands().size() > 0) {
                                int commandEnd = message.indexOf(' ');
                                String inputName;
                                if (commandEnd == -1)
                                    inputName = message.substring(1);
                                else
                                    inputName = message.substring(1, commandEnd);
                                for (Map.Entry<String, String> command : event.getConversation().commands().entrySet()) {
                                    if (inputName.equals(command.getKey())) {
                                        forEach(i -> i.onBotCommand(new CommandEvent(event, command.getKey(), command.getValue(), commandEnd)));
                                        return;
                                    }
                                }
                            }
                        }
                        forEach(i -> i.onMessage(event));
                        break;
                    default:
                        System.err.println("[" + getClass().getName() + "] New message type: " + data.getString("type"));
                }
                break;
            default:
                System.err.println("[" + getClass().getName() + "] New event type: " + data.getString("type"));
        }
//        data.getJson("pagination");
    }


    public void botReady(KeybaseBot self) {
        forEach(i -> i.onBotReady(self));
    }

    public void channelReady(Channel channel) {
        forEach(i -> i.onChannelReady(channel));
    }

    private int length = 0;
    private KeybaseEvent[] eventHandlers = new KeybaseEvent[2];

    private void forEach(Consumer<KeybaseEvent> action) {
        for (int i = 0; i < length; i++) {
            action.accept(eventHandlers[i]);
        }
    }

    public void addListener(KeybaseEvent event) {
        if (length + 1 == eventHandlers.length) {
            KeybaseEvent[] cache = new KeybaseEvent[(int) (eventHandlers.length * 1.5)];
            System.arraycopy(eventHandlers, 0, cache, 0, length);
        }
        eventHandlers[length++] = event;
    }

    public void setSender(DataSender sender) {
        this.sender = sender;
    }
}
