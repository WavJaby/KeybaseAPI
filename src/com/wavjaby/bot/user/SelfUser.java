package com.wavjaby.bot.user;

import com.wavjaby.bot.DataSender;
import com.wavjaby.json.JsonObject;

public class SelfUser extends User {
    private boolean configured;
    private boolean registered;
    private boolean loggedIn;
    private boolean sessionIsValid;

    public SelfUser(JsonObject json, DataSender sender) {
        super(json.getJson("user"), json.getString("deviceName"), sender);
        configured = json.getBoolean("configured");
        registered = json.getBoolean("registered");
        loggedIn = json.getBoolean("loggedIn");
        sessionIsValid = json.getBoolean("sessionIsValid");
    }

    public boolean getConfigured() {
        return configured;
    }

    public boolean getRegistered() {
        return registered;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public boolean getSessionIsValid() {
        return sessionIsValid;
    }
}
