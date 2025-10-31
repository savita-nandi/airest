package com.tigerdatademo.tigerdatatest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/similarsearch")
public class SimilaritySearchController {

	private final ChatClient chatClient;
	private final VectorStore vectorStore;
	
	
	public SimilaritySearchController(@Qualifier("chatClient") ChatClient chatClient,
			VectorStore vectorStore) {
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;		
	}
	
	
	/*
	 * URL: http://localhost:8080/similarsearch/getsimilaritysearch?message=Can you suggest me 3 aromatic wines
	 */
	@GetMapping("/getsimilaritysearch")
	public ResponseEntity<String> getsimilaritysearch(@RequestParam(value = "message", defaultValue = "Hello") String message) {
		
		SearchRequest searchRequest = SearchRequest.builder().query(message)
				.topK(3).build();				
		
		List<Document> documents = vectorStore.similaritySearch(searchRequest);
				
		String similaritySearchDocs = documents.stream().map(Document::getText)
				.collect(Collectors.joining(System.lineSeparator()));				
			
		return ResponseEntity.ok(similaritySearchDocs);		
	}
}
