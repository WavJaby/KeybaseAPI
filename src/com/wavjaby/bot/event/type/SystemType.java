package com.wavjaby.bot.event.type;

public enum SystemType {
    ADDED_TO_TEAM,
    INVITE_ADDED_TO_TEAM,

    UNKNOWN;

    public static SystemType valueOf(int index) {
        if (index >= values().length)
            return UNKNOWN;
        return values()[index];
    }
}
