package com.tigerdatademo.tigerdatatest;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MemoryChatConfig {

	/*
	 * @Bean ChatMemory chatMemory(JdbcChatMemoryRepository
	 * jdbcChatMemoryRepository) { return
	 * MessageWindowChatMemory.builder().maxMessages(10)
	 * .chatMemoryRepository(jdbcChatMemoryRepository).build(); }
	 */

    @Bean("chatMemoryChatClient")
    public ChatClient chatMemoryChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();//Custom class
        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor,tokenUsageAdvisor,memoryAdvisor))
                .build();
    }
    
    
    @Bean
    public ToolExecutionExceptionProcessor toolExecutionExceptionProcessorMemory() {    	
    	return new DefaultToolExecutionExceptionProcessor(false);
    }
    
}