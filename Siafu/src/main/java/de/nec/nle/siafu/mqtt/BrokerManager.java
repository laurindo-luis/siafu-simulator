package de.nec.nle.siafu.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import static org.apache.commons.lang.CharEncoding.UTF_8;

public class BrokerManager {
    private static Mqtt5BlockingClient client;

    public BrokerManager () {
        System.out.println("Conectando ao Broker...");
        try {
            final String host = "0863dadf94e94ed6aea53e56d4ac2b76.s1.eu.hivemq.cloud";
            final String username = "safepet";
            final String password = "Eduardo1996";

            //create an MQTT client
            client = MqttClient.builder()
                    .useMqttVersion5()
                    .serverHost(host)
                    .serverPort(8883)
                    .sslWithDefaultConfig()
                    .buildBlocking();

            //connect to HiveMQ Cloud with TLS and username/pw
            client.connectWith()
                    .simpleAuth()
                    .username(username)
                    .password(password.getBytes(UTF_8))
                    .applySimpleAuth()
                    .send();
            System.out.println("Conectado ao Broker com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void publish(String topic, String msg) {
        try {
            client.publishWith()
                    .topic(topic)
                    .payload(msg.getBytes(UTF_8))
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
