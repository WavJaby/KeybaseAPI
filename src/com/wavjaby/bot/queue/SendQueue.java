package com.wavjaby.bot.queue;

import com.wavjaby.bot.DataSender;

public class SendQueue {
    private final String message;
    private final DataSender sender;

    public SendQueue() {
        message = null;
        sender = null;
    }

    SendQueue(String message, DataSender sender){
        this.message = message;
        this.sender = sender;
    }

    void sendMessage(){
        sender.send(message, this);
    }

    public void onResult(String result){
    }
}
