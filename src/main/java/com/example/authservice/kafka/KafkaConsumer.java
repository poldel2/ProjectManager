package com.example.authservice.kafka;

import com.example.authservice.dto.LoginDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
public class KafkaConsumer {

    @KafkaListener(topics = "msg")
    public void orderListener(ConsumerRecord<Long, LoginDto> record) {
        System.out.println(record.partition());
        System.out.println(record.key());
        System.out.println(record.value());
    }
}