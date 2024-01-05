package org.example;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class ReplicatorProducer {
    public static void main(String[] args) throws InterruptedException {
        Properties prop = new Properties();
        try {
            FileInputStream input = new FileInputStream("/home/saran_dhandapani/plf-hackathon/task-1/client-cluster.properties");
            prop.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        final KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);
        Random random = new Random();

        System.out.println("started");
        for (int i = 0; i <= 10; i++) {
            String key = "key-" + (i % 4);
            ProducerRecord<String, String> record = new ProducerRecord<>("example-3" , key , Integer.toString(i));
            producer.send(record);
            Thread.sleep(1000 * 10);
        }

        System.out.println("Exited FOR loop");
        producer.flush();
        System.out.println("flushed");
        producer.close();
    }
}
