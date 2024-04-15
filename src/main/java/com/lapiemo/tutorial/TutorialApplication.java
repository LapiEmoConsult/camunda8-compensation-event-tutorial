package com.lapiemo.tutorial;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.Random;

@SpringBootApplication
@Deployment(resources = {"classpath:/**/*.bpmn", "classpath:/**/*.form"})
public class TutorialApplication {

	@Autowired
	private ZeebeClient zeebeClient;

	private final Random random = new Random();

	public static void main(String[] args) {
		SpringApplication.run(TutorialApplication.class, args);
	}

	@JobWorker(type = "send-email")
	public void sendEmail(ActivatedJob job) {
		System.out.println(job.getVariablesAsMap());
		System.out.println("Sent Email...");
	}
	@JobWorker(type = "process-payment")
	public void processPayment(ActivatedJob job) {
		System.out.println("Process payment...");

		zeebeClient.newSetVariablesCommand(job.getElementInstanceKey())
				.variables(Map.of("paymentSuccess", random.nextBoolean()))
				.send()
				.join();
	}
}
