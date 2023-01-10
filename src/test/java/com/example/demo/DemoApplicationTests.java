package com.example.demo;

import com.example.demo.dto.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rsocket.server.LocalRSocketServerPort;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

	private static RSocketRequester requester;
	@BeforeAll
	public static void setUpOnce(@Autowired RSocketRequester.Builder builder,
				   @LocalRSocketServerPort Integer port,
				   @Autowired RSocketStrategies strategies) {
		requester = builder
				.rsocketConnector(connector ->
					connector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2)))
				).dataMimeType(MimeTypeUtils.APPLICATION_JSON).tcp("localhost", port);
	}
	@Test
	void testRequestGetsResponse() {

		Mono<Message> response = requester
				.route("request-response")
				.data(new Message(("TEST")))
				.retrieveMono(Message.class);

		StepVerifier
				.create(response)
				.consumeNextWith(message -> {
					assertThat(message.getMessage()).isEqualTo("You said: TEST");
				})
				.verifyComplete();
	}

}
