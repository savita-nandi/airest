package com.tigerdatademo.tigerdatatest;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Webconfig {

	@Bean(name ="customChatClient")
	public ChatClient customChatClient(ChatClient.Builder chatClientBuilder, 
			RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
		
		Advisor loggerAdvisor = new SimpleLoggerAdvisor();
		Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();//Custom class
		
		return chatClientBuilder
				.defaultAdvisors(List.of(loggerAdvisor,tokenUsageAdvisor, 
						retrievalAugmentationAdvisor)).build();				
	}
	
	/*This chatClient is used for translating UserPrompt in different language
	 * to English*/
	@Bean(name ="translationChatClient")
	public ChatClient translationChatClient(ChatClient.Builder chatClientBuilder, 
			RetrievalAugmentationAdvisor ragAdvisorQueryTransformer) {
		
		Advisor loggerAdvisor = new SimpleLoggerAdvisor();
		Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();//Custom class
		
		return chatClientBuilder
				.defaultAdvisors(List.of(loggerAdvisor,tokenUsageAdvisor, 
						ragAdvisorQueryTransformer)).build();				
	}
	
	
	@Bean(name ="chatClient")
	public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
		
		Advisor loggerAdvisor = new SimpleLoggerAdvisor();
		Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();//Custom class
		
		return chatClientBuilder
				.defaultAdvisors(List.of(loggerAdvisor,tokenUsageAdvisor)).build();				
	}
	
	/*Similarity threshold matching criteria of 70% */
	@Bean
	public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore) {
		
		return 	RetrievalAugmentationAdvisor
				.builder()
				.documentRetriever(VectorStoreDocumentRetriever.builder()
						.vectorStore(vectorStore).topK(3).similarityThreshold(0.7).build())
				.build();			
	}
	
	
	
	/*Similarity threshold matching criteria of 70% */
	@Bean
	public RetrievalAugmentationAdvisor ragAdvisorQueryTransformer(VectorStore vectorStore,
			ChatClient.Builder chatClientBuilder) {
		
		return 	RetrievalAugmentationAdvisor
				.builder()
				.queryTransformers(TranslationQueryTransformer.builder().chatClientBuilder(chatClientBuilder.clone())
						.targetLanguage("english").build())
				.documentRetriever(VectorStoreDocumentRetriever.builder()
						.vectorStore(vectorStore).topK(3).similarityThreshold(0.7).build())
				.build();			
	}
	
}
