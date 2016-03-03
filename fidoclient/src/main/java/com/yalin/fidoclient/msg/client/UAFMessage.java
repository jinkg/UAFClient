package com.yalin.fidoclient.msg.client;


public class UAFMessage extends JsonSerializable {
    public String uafProtocolMessage;

    public UAFMessage(){
    }

    public UAFMessage(String uafProtocolMessage) {
        this.uafProtocolMessage = uafProtocolMessage;
    }

    @Override
    public String toJson() {
        return gson.toJson(this);
    }

    @Override
    public void loadFromJson(String json) {
        UAFMessage message = gson.fromJson(json, UAFMessage.class);
        this.uafProtocolMessage = message.uafProtocolMessage;
    }
}
