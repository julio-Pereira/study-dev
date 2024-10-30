package com.juliopereira.kafkaproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ApacheKafkaJavaProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApacheKafkaJavaProducerApplication.class, args);
	}

	@RestController
	@RequestMapping("/kafka")
	class ApacheKafkaWebController {
	    private final KafkaProducer producer;

	    public ApacheKafkaWebController(KafkaProducer producer) {
	        this.producer = producer;
	    }

	    @RequestMapping(value = "/publish/{name}")
	    public String sendMessageToKafkaTopic(@PathVariable("name") String name) {
	        producer.sendMessage("Hello " + name);
	        return "Message sent to the Kafka Topic test-topic Successfully";
	    }
	}

}
