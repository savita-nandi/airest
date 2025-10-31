package com.tigerdatademo.tigerdatatest.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memory")
public class ChatMemoryController {

	
	private final ChatClient chatClient;
	
	public ChatMemoryController(@Qualifier("chatMemoryChatClient") ChatClient chatClient) {
		this.chatClient = chatClient;		
	}
	
	
	
 	/* http://localhost:8080/memory/getchatmemory?message=My name is Savitha
	 *
	 * */
	@GetMapping("/getchatmemory")
	public ResponseEntity<String> getChatMemory(@RequestParam(value = "message", 
	defaultValue = "Hello") String message) {
	
		String response = chatClient.prompt()
				.user(message)
				.call()
				.content();		
		return ResponseEntity.ok(response);	
	}
}
