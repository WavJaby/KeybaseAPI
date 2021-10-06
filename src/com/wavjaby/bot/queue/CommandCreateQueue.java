package com.wavjaby.bot.queue;

import com.wavjaby.bot.DataSender;
import com.wavjaby.bot.message.MessageObject;

public class CommandCreateQueue extends SendQueue {
    private CommandCreateEvent commandCreateEvent;

    public CommandCreateQueue(String message, DataSender sender) {
        super(message, sender);
    }

    public void queue(CommandCreateEvent commandCreateEvent) {
        this.commandCreateEvent = commandCreateEvent;
        sendMessage();
    }

    public void queue() {
        sendMessage();
    }

    @Override
    public void onResult(String result) {
        System.out.println(result);
//        if(commandCreateEvent != null)
//            commandCreateEvent.onmessageSend(new JsonObject(result));
    }

    interface CommandCreateEvent {
        void onmessageSend(MessageObject message);
    }
}
