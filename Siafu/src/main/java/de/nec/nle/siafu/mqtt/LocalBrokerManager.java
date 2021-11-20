package de.nec.nle.siafu.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;

public class LocalBrokerManager {

    IMqttClient client = null;

    public LocalBrokerManager() {
        try {
            String clientId = UUID.randomUUID().toString();
            client = new MqttClient("tcp://192.168.18.12", "siafuSimulator");
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(true);
            client.connect(mqttConnectOptions);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String msg) {

        try {
            MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
