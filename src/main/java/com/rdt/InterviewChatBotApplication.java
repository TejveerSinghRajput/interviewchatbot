package com.rdt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InterviewChatBotApplication {

	public static void main(String[] args) {
		try{
			SpringApplication.run(InterviewChatBotApplication.class, args);
			System.out.println("Interview Chat Bot Application started successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
