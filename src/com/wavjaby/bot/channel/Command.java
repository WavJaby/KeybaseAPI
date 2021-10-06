package com.wavjaby.bot.channel;

public class Command {
    private String name;
    private String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    String toData() {
        return "{\"name\": \"" + name + "\", \"description\": \"" + description + "\"}";
    }
}
