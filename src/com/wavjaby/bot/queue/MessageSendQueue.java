package com.wavjaby.bot.queue;

import com.wavjaby.bot.DataSender;
import com.wavjaby.bot.message.MessageObject;

public class MessageSendQueue extends SendQueue {
    private MessageSendEvent messageSendEvent;

    public MessageSendQueue(String message, DataSender sender) {
        super(message, sender);
    }

    public void queue(MessageSendEvent messageSendEvent) {
        this.messageSendEvent = messageSendEvent;
        sendMessage();
    }

    public void queue() {
        sendMessage();
    }

    @Override
    public void onResult(String result) {
        System.out.println("message send");
    }

    interface MessageSendEvent{
        void onmessageSend(MessageObject message);
    }
}
