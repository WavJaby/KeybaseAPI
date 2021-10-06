package com.wavjaby.bot.queue;

import com.wavjaby.bot.DataSender;
import com.wavjaby.json.JsonObject;

public class FileSendQueue extends SendQueue {
    private FileSendEvent fileSendEvent;

    public FileSendQueue(String message, DataSender sender) {
        super(message, sender);
    }

    public void queue(FileSendEvent fileSendEvent) {
        this.fileSendEvent = fileSendEvent;
        sendMessage();
    }

    public void queue() {
        sendMessage();
    }

    @Override
    public void onResult(String result) {
        if(fileSendEvent != null)
        fileSendEvent.onFileSend(new JsonObject(result));
    }

    public interface FileSendEvent {
        void onFileSend(JsonObject message);
    }
}
