package com.tigerdatademo.tigerdatatest.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/unittest")
public class UnitTestChatController {

	@Value("classpath:/promptTemplates/hrPolicy.st")
    Resource hrPolicyTemplate;
	
	private final ChatClient chatClient;
	
	public UnitTestChatController(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder
							.defaultAdvisors(new SimpleLoggerAdvisor())
							.build();
	}
	
	
	/*URL:  http://localhost:8080/unittest/chat?message=What is the current time in Bengaluru, India */
	@GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return chatClient.prompt().user(message)
                .call().content();
    }

    
	/*URL:  http://localhost:8080/unittest/prompt-stuffing?message=What is the current time in Bengaluru, India 
	 * */
	@GetMapping("/prompt-stuffing")
    public String promptStuffing(@RequestParam("message") String message) {
        return chatClient
                .prompt().system(hrPolicyTemplate)
                .user(message)
                .call().content();
    }
	
}
