package org.example;

import io.confluent.connect.replicator.offsets.ConsumerTimestampsInterceptor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ReplicatorConsumer {

    public static void main(String[] args) throws InterruptedException {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("/home/saran_dhandapani/plf-hackathon/task-1/client-cloud.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-4r087.us-west2.gcp.confluent.cloud:9092");
        prop.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "ConsumersExample3");
        prop.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        prop.setProperty(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, ConsumerTimestampsInterceptor.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Arrays.asList("example-3"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("New records: \n" +
                        "Key: " + record.key() + ", " +
                        "Value: " + record.value() + ", " +
                        "Topic: " + record.topic() + ", " +
                        "Offset: " + record.offset()  + "\n");
                Thread.sleep(1000 * 20);
                consumer.commitSync();
            }
        }
    }
}
