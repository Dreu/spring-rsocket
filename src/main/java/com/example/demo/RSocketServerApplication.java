package com.example.demo;

import com.example.demo.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootApplication
@Controller
public class RSocketServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RSocketServerApplication.class, args);
	}

	@MessageMapping("request-response")
	Mono<Message> requestResponse(final Message message) {
		log.info("Receive request-response message: {}", message);
		return Mono.just(new Message("You said: " + message.getMessage() ));
	}

	@MessageMapping("fire-and-forger")
	Mono<Void> fireAndForget(final Message message) {
		log.info("Received fire-and-forget request: {}", message);
		return Mono.empty();
	}

}
