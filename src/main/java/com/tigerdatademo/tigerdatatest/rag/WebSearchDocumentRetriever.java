package com.tigerdatademo.tigerdatatest.rag;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;


public class WebSearchDocumentRetriever implements DocumentRetriever {
    
	private static final Logger logger = LoggerFactory.getLogger(WebSearchDocumentRetriever.class);  
    private static final int DEFAULT_RESULT_LIMIT = 5;       
    private final RestClient tavilyRestClient;
    private int resultLimit;    
	
	 
	 public WebSearchDocumentRetriever(@Qualifier("tavilyRestClientBuilder") 
	 	RestClient.Builder tavilyRestClientBuilder, int resultLimit) {	
		 
		 this.tavilyRestClient = tavilyRestClientBuilder.build();
		 this.resultLimit = resultLimit;
	 }
    	

    /*
     * Retrieves query results from the Web based, based on maxResults and on UserPrompt=Query 
     * 
     */
    @Override
    public List<Document> retrieve(Query query) {
    	
    	logger.info("retrieve(): Processing query: {}", query.text());
        Assert.notNull(query, "query cannot be null");

        String q = query.text();
        Assert.hasText(q, "query.text() cannot be empty");
        
        logger.info("retrieve(): Before tavilyRestClient.post()");        
        TavilyResponsePayload response = this.tavilyRestClient.post()
                .body(new TavilyRequestPayload(q, "advanced", resultLimit))
                .retrieve()
                .body(TavilyResponsePayload.class);
        
        logger.info("retrieve(): After tavilyRestClient.post(), response : " + response.results().size());
        
        if (response == null || CollectionUtils.isEmpty(response.results())) {
            return List.of();
        }

        List<Document> docs = new ArrayList<>(response.results().size());
        for (TavilyResponsePayload.Hit hit : response.results()) {
            // Map each Tavily hit into a Spring AI Document with metadata and score.
            Document doc = Document.builder()
                    .text(hit.content())
                    .metadata("title", hit.title())
                    .metadata("url", hit.url())
                    .score(hit.score())
                    .build();
            docs.add(doc);
        }
        return docs;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record TavilyRequestPayload(String query, String searchDepth, int maxResults) {}

    record TavilyResponsePayload(List<Hit> results) {
        record Hit(String title, String url, String content, Double score) {}
    }

    public static Builder builder() {
        return new Builder();
    }
    
    
    /*Inner class based on Builder design pattern*/
    public static class Builder {
        private RestClient.Builder clientBuilder;
        private int resultLimit = DEFAULT_RESULT_LIMIT;
       
        
        private Builder() {}

        public Builder restClientBuilder(RestClient.Builder clientBuilder) {
            this.clientBuilder = clientBuilder;
            return this;
        }
                
        public Builder maxResults(int maxResults) {
            if (maxResults <= 0) {
                throw new IllegalArgumentException("WebSearchDocumentRetriever->Builder():maxResults must be greater than 0");
            }
            this.resultLimit = maxResults;
            return this;
        }

        public WebSearchDocumentRetriever build() {        	
        	return new WebSearchDocumentRetriever(clientBuilder, resultLimit);
        }
    }
}