package com.tigerdatademo.tigerdatatest;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import com.tigerdatademo.tigerdatatest.rag.WebSearchDocumentRetriever;

@Configuration
public class WebSearchRagConfig {

	@Value("${spring.ai.tavily.api-key}")
    private String tavilyApiKey;
    
    @Value("${spring.ai.tavily.url}")
    private String tavilyUrl;
	
	@Bean(name ="webSearchRagChatClient")
	public ChatClient customChatClient(ChatClient.Builder chatClientBuilder, 
			RestClient.Builder clientBuilder) {
		
		Advisor loggerAdvisor = new SimpleLoggerAdvisor();
		Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();//Custom class
		
		RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = 
				RetrievalAugmentationAdvisor.builder()
		.documentRetriever(WebSearchDocumentRetriever.builder()
				.restClientBuilder(clientBuilder).maxResults(5)
				.build()).build();
		
		
		return chatClientBuilder.defaultAdvisors(
				List.of(loggerAdvisor, tokenUsageAdvisor,retrievalAugmentationAdvisor))
				.build();		
	}
	
	
	  @Bean(name="tavilyRestClientBuilder")
	  public RestClient.Builder getTavilyRestClientBuilder() { 		
		  RestClient.Builder builder = RestClient.builder();
		  return builder.baseUrl(tavilyUrl) 
				  .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tavilyApiKey); 
		  
		  }
	 
}
