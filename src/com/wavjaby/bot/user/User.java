package com.wavjaby.bot.user;

import com.wavjaby.bot.DataSender;
import com.wavjaby.json.JsonObject;

public class User {
    private final String id; //uuid
    private String name;
    private String deviceName;
    private String deviceID;
    private DeviceType deviceType;
    private long createTime;

    public User(JsonObject json, DataSender sender) {
        id = json.getString("uid");
        name = json.getString("username");
        deviceName = json.getString("device_name");
        deviceID = json.getString("device_id");
        getDevice(sender);
    }

    User(JsonObject json, String deviceName, DataSender sender) {
        id = json.getString("uid");
        name = json.getString("username");
        this.deviceName = deviceName;
        getDevice(sender);
    }

    private void getDevice(DataSender sender) {
        JsonObject result = new JsonObject(sender.send("{\"method\": \"getdeviceinfo\", \"params\": {\"options\": {\"username\": \"" + name + "\"}}}"));
        for (Object i : result.getJson("result").getArray("devices")) {
            JsonObject data = (JsonObject) i;
            if (data.getString("description").equals(deviceName)) {
                deviceID = data.getString("id");
                deviceType = DeviceType.valueOf(data.getString("type"));
                createTime = data.getLong("ctime");
                break;
            }
        }
    }

    public long getCreateTime() {
        return createTime;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceID() {
        return deviceID;
    }

    @Override
    public boolean equals(Object obj) {
        return ((User) obj).id.equals(this.id);
    }
}
