package com.wavjaby.bot.event;

import com.wavjaby.bot.event.type.Event;

public class CommandEvent extends Event {
    private final String command;
    private final String description;
    private String[] args;

    CommandEvent(MessageEvent event, String command, String description, int argsStart) {
        super(event.getConversation(), event.getChannel(), event.getSender(), event.getTimeStampSec(), event.getTimeStamp());
        this.command = command;
        this.description = description;
        if (argsStart == -1 || argsStart == event.getMessageText().length() - 1)
            args = null;
        else
            args = event.getMessageText().substring(argsStart + 1).split(" ");
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String[] getArgs() {
        return args;
    }
}
