package com.wavjaby.bot.user;

import com.wavjaby.bot.DataSender;
import com.wavjaby.json.JsonObject;

public class Sender extends User {
    public Sender(JsonObject json, DataSender sender) {
        super(json, sender);
    }
}
