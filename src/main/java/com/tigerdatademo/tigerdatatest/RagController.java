package com.tigerdatademo.tigerdatatest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.vectorstore.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;



@RestController
@RequestMapping("/rag")
public class RagController {

	
	@Value("classpath:/promptTemplates/systemPromptWinedataTemplate.st")
	Resource promptTemplate;
	
	@Value("classpath:/promptTemplates/systemPromptHrPolicyTemplate.st")
	private Resource sysPromptHrPolicyTemplate;
	
	private final ChatClient chatClient;
	private final ChatClient webSearchRagChatClient;
	private final ChatClient translationChatClient;
	private final VectorStore vectorStore;
	
	
	public RagController(@Qualifier("customChatClient") ChatClient chatClient,
			@Qualifier("webSearchRagChatClient") ChatClient webSearchRagChatClient,
			@Qualifier("translationChatClient") ChatClient translationChatClient,
			VectorStore vectorStore) {
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;		
		this.webSearchRagChatClient = webSearchRagChatClient;
		this.translationChatClient = translationChatClient;
	}
	
			
	/*
	 * URL: http://localhost:8080/rag/getragchat?message=Can you suggest me 3 aromatic wines
	 * Method uses SystemPromptTemplate which is populated with SimilaritySearchResults 
	 * and then the promptTemplate is sent to LLM
	 */
	@GetMapping("/getragchat")
	public ResponseEntity<String> getRagChat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
		
		/*
		 * SearchRequest searchRequest = SearchRequest.builder().query(message)
		 * .topK(3).similarityThreshold(0.5).build();
		 */
		SearchRequest searchRequest = SearchRequest.builder().query(message)
				.topK(3).build();		
		
		List<Document> documents = vectorStore.similaritySearch(searchRequest);
		
		String similaritySearchDocs = documents.stream().map(Document::getText)
				.collect(Collectors.joining(System.lineSeparator()));		
		
		/*Loading the SystemPromptTemplate of Resources folder*/
		SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(this.promptTemplate);
		Prompt prompt = new Prompt(List.of(
				systemPromptTemplate.createMessage(Map.of("documents", similaritySearchDocs)),
				new UserMessage(message)));
		
		String response = chatClient.prompt(prompt).call().content();		
		return ResponseEntity.ok(response);		
	}
	
	
	/*
	 * URL: http://localhost:8080/rag/getragchat1?message=Can you suggest me 3 aromatic wines
	 * Method uses, RetrievalAugmentationAdvisor, that Augments the search results from the VectorDB
	 * applying the filter criteria and then passess those AUgmented results(Context) and UserQuery
	 * to LLM for further refinement 
	 */
	@GetMapping("/getragchat1")
	public ResponseEntity<String> getRagChat1(@RequestParam(value = "message", defaultValue = "Hello") String message) {
		
		/*The chatClient Bean is configured with RetrievalAugmentationAdvisor that has the 
		 * SearchRequest criteria of topK and SimilarityThreshold%*/
		String response = chatClient.prompt().user(message).call().content();
		return ResponseEntity.ok(response);		
	}
	
	
	
	/*
	 * URL: http://localhost:8080/rag/getragdocumentchat?message=Tell me about Notice period
	 * Method uses SystemPromptTemplate which is populated with SimilaritySearchResults 
	 * and then the promptTemplate is sent to LLM
	 */
	@GetMapping("/getragdocumentchat")
	public ResponseEntity<String> getRagDocumentChat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
		
		SearchRequest searchRequest = SearchRequest.builder().query(message)
				.topK(3).build();						
		List<Document> documents = vectorStore.similaritySearch(searchRequest);		
		
		String similaritySearchDocs = documents.stream().map(Document::getText)
				.collect(Collectors.joining(System.lineSeparator()));				
		
		/*Loading the SystemPromptTemplate of Resources folder*/
		SystemPromptTemplate systemPromptTemplate = 
				new SystemPromptTemplate(this.sysPromptHrPolicyTemplate);
		
		Prompt prompt = new Prompt(List.of(
				systemPromptTemplate.createMessage(Map.of("documents", similaritySearchDocs)),
				new UserMessage(message)));
		
		String response = chatClient.prompt(prompt).call().content();				
		return ResponseEntity.ok(response);		
	}
	
	/*
	 * URL: http://localhost:8080/rag/getragwebsearchchat?message=How is the US stock market performing today
	 * Method uses, RetrievalAugmentationAdvisor that uses a custom 
	 * DocumentRetreiver->WebSearchDocumentRetreiver which in turn uses 3rd party 
	 * WebSearch Engine->TAVILY that searches the Web applying the filter criteria of MaxResultPages
	 * and then the AugmentedResults(context) is sent to LLM along with UserQuery for further refinement
	 */
	@GetMapping("/getragwebsearchchat")
	public ResponseEntity<String> getRagWebSearchChat(
			@RequestParam(value = "message", defaultValue = "Hello") String message) {
		
		/*The chatClient Bean is configured with RetrievalAugmentationAdvisor that has 
		 * the SearchRequest criteria of maxResults from Websearch*/
		String response = webSearchRagChatClient.prompt().user(message).call().content();
		return ResponseEntity.ok(response);	
	}
	
	
	/* English URL: http://localhost:8080/rag/getragtranslatedchat?message=Tell me about Notice period
	 * Kannada URL: http://localhost:8080/rag/getragtranslatedchat?message=ಸೂಚನೆ ಅವಧಿಯ ಬಗ್ಗೆ ಹೇಳಿ
	 * This is Advanced RAG :Method uses, TranslationQueryTransformer that translates the Kannada query on Notice period to 
	 * English as part of the Pre-Rertrieval stage and sends translated query to RetrievalAugmentationAdvisor
	 * which has the searchCriteria and applies it on the DOcs in VectorStore Db 
	 * and then the AugmentedResults(context) is sent to LLM along with UserQuery for further refinement
	 */
	@GetMapping("/getragtranslatedchat")
	public ResponseEntity<String> getRagTranslatedChat(
			@RequestParam(value = "message", defaultValue = "Hello") String message) {
		
		/*The chatClient Bean is configured with RetrievalAugmentationAdvisor that has 
		 * the SearchRequest criteria of maxResults from Websearch*/
		String response = translationChatClient.prompt().user(message).call().content();
		return ResponseEntity.ok(response);	
	}
	
}
