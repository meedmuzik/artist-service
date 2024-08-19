package org.scuni.artistservice.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.BinderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class KafkaConfiguration {

    @Bean
    public Supplier<String> produce() {
        return () -> {
            // generate message content
            String message = "Hello, Kafka!";
            return message;
        };
    }

    @Bean
    public Consumer<String> consume() {
        return message -> {
            // process message content
           log.info(message);
        };
    }

}
