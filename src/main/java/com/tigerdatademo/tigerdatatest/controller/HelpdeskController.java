package com.tigerdatademo.tigerdatatest.controller;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tigerdatademo.tigerdatatest.tools.HelpDeskTools;

@RestController
@RequestMapping("/helpdesk")
public class HelpdeskController {

	private final ChatClient chatClient;
	private final HelpDeskTools helpDeskTools;
	
	public HelpdeskController(@Qualifier("heldeskChatClient") ChatClient chatClient,
			HelpDeskTools helpDeskTools) {
		this.chatClient = chatClient;	
		this.helpDeskTools = helpDeskTools;	
	}
	
	
	/* http://localhost:8080/helpdesk/gethelpdesk?message=What is the status of my ticket 
	 
	 * */
	@GetMapping("/gethelpdesk")
	public ResponseEntity<String> getHelpdesk(@RequestHeader("username") String username,
			@RequestParam(value = "message", defaultValue = "Hello") String message) {
	
		String response = chatClient.prompt()
				.user(message)
				.tools(helpDeskTools)
				.toolContext(Map.of("username", username))
				.call()
				.content();		
		return ResponseEntity.ok(response);	
	}
	
}
