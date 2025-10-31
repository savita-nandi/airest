package com.tigerdatademo.tigerdatatest.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tools")
public class TimeController {
	
	private final ChatClient chatClient;
	
	
	public TimeController(@Qualifier("timeChatClient") ChatClient chatClient) {
		this.chatClient = chatClient;		
	}
	
	/* http://localhost:8080/tools/getlocaltime?message=What is the current time in Bengaluru, India 
	 * http://localhost:8080/tools/getlocaltime?message=Get the current time of my location 
	 * http://localhost:8080/tools/getlocaltime?message=What is the current time in Bengaluru, India
	 * http://localhost:8080/tools/getlocaltime?message=Get the current time in Texas 
	 * */
	@GetMapping("/getlocaltime")
	public ResponseEntity<String> getLocalTime(@RequestParam(value = "message", 
	defaultValue = "Hello") String message) {
	
		String response = chatClient.prompt()
				.user(message)
				.call()
				.content();		
		return ResponseEntity.ok(response);	
	}
	
}
