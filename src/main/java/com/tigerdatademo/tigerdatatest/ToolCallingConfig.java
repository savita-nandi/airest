package com.tigerdatademo.tigerdatatest;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.tigerdatademo.tigerdatatest.tools.TimeTools;

@Configuration
public class ToolCallingConfig {

	@Value("classpath:/promptTemplates/helpDeskSystemPromptTemplate.st")
	private Resource helpdeskSysPrompt;
	
	@Bean(name ="timeChatClient")
	public ChatClient customChatClient(ChatClient.Builder chatClientBuilder,
			TimeTools timeTools) {
		
		Advisor loggerAdvisor = new SimpleLoggerAdvisor();
		Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();//Custom class
		
		return chatClientBuilder
				.defaultTools(timeTools)
				.defaultAdvisors(List.of(loggerAdvisor,tokenUsageAdvisor)).build();				
	}
	
	
	@Bean(name ="heldeskChatClient")
	public ChatClient heldeskChatClient(ChatClient.Builder chatClientBuilder,
			TimeTools timeTools, ChatMemory chatMemory) {
		
		Advisor loggerAdvisor = new SimpleLoggerAdvisor();
		Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();//Custom class
		 Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
		return chatClientBuilder
				.defaultSystem(helpdeskSysPrompt)
				.defaultTools(timeTools)
				.defaultAdvisors(List.of(loggerAdvisor,tokenUsageAdvisor,memoryAdvisor)).build();				
	}
	
	/*This methos will always send the Exception message to CLient if value is set to True*/
	@Bean
	public ToolExecutionExceptionProcessor toolExecutionExceptionProcessor() {		
		return new DefaultToolExecutionExceptionProcessor(false);
	}
	
	
}
