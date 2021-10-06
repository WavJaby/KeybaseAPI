package com.wavjaby.bot.event;

import com.wavjaby.bot.DataSender;
import com.wavjaby.bot.KeybaseBot;
import com.wavjaby.bot.event.type.Event;
import com.wavjaby.json.JsonObject;

public class JoinTeamEvent extends Event {
    public JoinTeamEvent(JsonObject message, JsonObject json, KeybaseBot bot, DataSender dataSender) {
        super(message, bot, dataSender);
    }
}
