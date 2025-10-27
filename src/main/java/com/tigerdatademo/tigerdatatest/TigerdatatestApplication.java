package com.tigerdatademo.tigerdatatest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import com.tigerdatademo.tigerdatatest.model.VectorstoreRepo;
import com.tigerdatademo.tigerdatatest.model.VectorstoreTbl;
import com.tigerdatademo.tigerdatatest.model.Winedocs;
import com.tigerdatademo.tigerdatatest.model.Winedocs2;
import com.tigerdatademo.tigerdatatest.model.Winedocs2Repo;
import com.tigerdatademo.tigerdatatest.model.WinedocsRepo;

/*3Sep2025:

Reference: Spring + vectorize postgresql 17 data sample code in main method
 * org.springframework.ai.embedding.EmbeddingClient
 * */

@SpringBootApplication
@ComponentScan(basePackages = {"com.tigerdatademo.tigerdatatest","com.tigerdatademo.tigerdatatest.model"})
public class TigerdatatestApplication {

	private static final String DB_URL = "jdbc:postgresql://w7c25hbfqu.e491z0j1i7.tsdb.cloud.timescale.com:33821/tsdb?sslmode=require";
	private static final String USER = "tsdbadmin";
	private static final String PASSWORD = "admin1234567";
	private static final String JDBC_DRIVER = "org.postgresql.Driver"; // "com.mysql.cj.jdbc.Driver";

	
	public static void main(String[] args) {
		
		SpringApplication.run(TigerdatatestApplication.class, args);		
		/*
		 * ConfigurableApplicationContext context =
		 * SpringApplication.run(TigerdatatestApplication.class, args); 
		 * VectorStore vectorStore = context.getBean(PgVectorStore.class);
		*/
		
		//LOgin to Url=https://console.cloud.timescale.com/dashboard/services/w7c25hbfqu/sql_editor[Haystack249@]
		//Table Query=select * from winedocs_2;
		//strQuery="Desc=Aromas include tropical fruit"; Desc=Blackberry and raspberry aromas show
		//strQuery="Title=Nicosia 2013 Vulkà Bianco  (Etna); Title="Quinta dos Avidagos 2011 Avidagos Red (Douro)";
		String strQuery = "";
		
		if(args.length==0) {
			strQuery="Balanced with acidity"; //Default value if no args passed
		}		
		for (String arg : args) {
			strQuery = strQuery + " " + arg;
		}
		
		/*
		 * System.out.println("Inside method main: VectorStore is " + vectorStore +
		 * " and Args=" + strQuery);
		 * 
		 * System.out.println("Inside method main: TigerdatatestApplication started " +
		 * "and context fetched:" + context.getDisplayName());
		 */

		//*** Generate Embeddings and store in vector_store table.Done for 10 records only
		
		
		/*
		 * WinedocsRepo winedocsRepo = context.getBean(WinedocsRepo.class);
		 * generateEmbeddings(winedocsRepo, vectorStore);
		 */
		 
		 		
		
		//*** Perform Similarity using Spring AI VectorStore				
				 
		//performSimilaritySearch(vectorStore, strQuery);
		 
		 		 
		
		//*** Perform Similarity using Spring AI VectorStore with an additional filter		
		
		//performSimilaritySearchWithFilter(vectorStore, strQuery);
		 
		 		 
		
		//*** FullText Search using LIKE %value%.However this method returns no results
		/*
		 * VectorstoreRepo vectorstoreRepo = context.getBean(VectorstoreRepo.class);
		 * performFullTextSearch(vectorstoreRepo, strQuery);
		 */
		
		
		//*** FullText Search using TSVector & Gin index & native Query
		//This directly queries Table, and does not use VectorStore/OPenApiAI and thus no cost to OpenApiAI
		/*
		 * Winedocs2Repo winedocs2Repo = context.getBean(Winedocs2Repo.class);
		 * performFullTextSearchTsVector(winedocs2Repo, strQuery);
		 */
		 		
		//Cannot close as this line will close the context and springboot app, which is still executing
		//((ConfigurableApplicationContext) context).close(); 
		
	}
	

	private static void generateEmbeddings(WinedocsRepo winedocsRepo, VectorStore vectorStore) {
		List<Winedocs> winedocsList = winedocsRepo.findWineDocsNotEmbedded();
		
		List<Document> documents = new ArrayList();

		//Preconditions: import csv into windocs table.Create vector_store table with indexes
		//Loop through the records in winedocs table and concatenate the columns that needs to be vectorized. 
		//Vectore.add() method will generate embeddings/dimensions and inserts the following 
		//columns into Vector_Store table: 
		//ConcatenatedText=Content column, title=metadata column, inMemoryGenerated embeddings=embedding column 
				
		for (Winedocs w : winedocsList) {
			
			String textToEmmbed =  w.getTitle() + "#" + w.getDescription() ;
			//String textToEmmbed =  w.getContent() ;
			
			//Document doc = new Document(textToEmmbed);
			Document doc = new Document(textToEmmbed, Map.of("title", w.getTitle()));
			
			documents.add(doc);

			/*
			 * String textToEmmbed = w.getDescription() + " " + w.getTitle();
			 * EmbeddingClient embeddingClient = new EmbeddingClient();
			 * 
			 * // Generate embedding using Spring AI's EmbeddingClient List<Double>
			 * embeddingList = embeddingClient.embed(textToEmmbed);
			 * 
			 * // Convert List<Double> to float[] for storage in PostgreSQL float[]
			 * embeddingArray = new float[embeddingList.size()]; for (int i = 0; i <
			 * embeddingList.size(); i++) { embeddingArray[i] =
			 * embeddingList.get(i).floatValue(); }
			 * 
			 * w.setEmbedding(embeddingArray); winedocsRepo.save(w);
			 * System.out.println("Updated embedding for doc id: " + w.getId());
			 */
		}
		
		/*DOcs to be vectorized is 7500*/
		System.out.println("Documents to be vectorized, Size is: " + documents.size());
		
		/*vector_store table if already has records then below lines will create duplicate rows.
		 * Hence truncate from VS table and then execute below line.
		 * Note that records are looped through winedocs table, added to a List of AI.Document
		 * and then the below line inserts it into VS table.
		 * The below line inserts records into columns=content, metadata,embedding. 
		 * Takes 5-8 mins for 7500 rows*/		
		vectorStore.add(documents);
		
		System.out.println("Documents added to Vector Store");
	}
	
	
	/*performFullTextSearch: Here full-text search is based on plain-simple equation text ie LIKE %value%. 
	 * However this method returns no results
	 * */	
	
	  private static void performFullTextSearch(VectorstoreRepo
	  vectorstoreRepo,String argValue) {
	  
	  System.out.println("performFullTextSearch Start"); List<VectorstoreTbl>
	  vectorstoreTblList = vectorstoreRepo.findByFullTextSearch(argValue);
	  System.out.println("performFullTextSearch Complete, Size is: " +
	  vectorstoreTblList.size());
	  
	  
	  for(VectorstoreTbl v: vectorstoreTblList) {
	  System.out.println("FullText Search Results are: id= " + v.getId() +
	  ", content= " + v.getContent()); } }
	 
	
	
	 /*FullText Search using TSVector & Gin index & native Query: 
	  * https://medium.com/@svosh2/postgres-full-text-search-spring-boot-integration-13cbeb7af570#id_token=eyJhbGciOiJSUzI1NiIsImtpZCI6IjljNjI1MTU4Nzk1MDg0NGE2NTZiZTM1NjNkOGM1YmQ2Zjg5NGM0MDciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIyMTYyOTYwMzU4MzQtazFrNnFlMDYwczJ0cDJhMmphbTRsamRjbXMwMHN0dGcuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIyMTYyOTYwMzU4MzQtazFrNnFlMDYwczJ0cDJhMmphbTRsamRjbXMwMHN0dGcuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDk5OTg1MzEyNjE0ODQyMzk1NzkiLCJlbWFpbCI6InNhdml0YS5uYW5kaUBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmJmIjoxNzU3MzkyNDY5LCJuYW1lIjoiU2F2aXRoYSBCUyIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NKa2VoSUdudzlmeDI0UkJKVTdZbmJmVXY4aGZDQzB4ZW00dzd2aEp1Tm9PdTlIVjZEZD1zOTYtYyIsImdpdmVuX25hbWUiOiJTYXZpdGhhIiwiZmFtaWx5X25hbWUiOiJCUyIsImlhdCI6MTc1NzM5Mjc2OSwiZXhwIjoxNzU3Mzk2MzY5LCJqdGkiOiI2NjA1MDVkMGZmZjI2YmFkOGVjNzY5Y2Y0Y2ZkZDBkZjMyZDM1YmYxIn0.36tfsmI8_FprmDZ9eGqIjIhPQSZNVPD6MQ9vk98kIQKselxNyZ_9R8JiXiGoz6U0572ggeyHV7wVck5WWla4_RjO9zhm2--nCZtWktoiWWx7-vIxjw6lTSFTRmeMcBWAq7EUhgIPJmkwUROJ55kz99xZubyCyZ_-wu9ChKHSfgo6tkrRihdhnY5e7qCAdhbYv1EBJQFl0RI5U5JxmEGR2iQ6pQk4BnmsfXNVnGPSlyAJSRwUwzdJpRRiY8-p-GnY0KF6qqxPYT0NOmwpxVlbuTeozL1NOfAgAzWPNnWCDUtCvMvirw2fy3_Ti3ZJ1IN9_J1AHyiw_Yh39UivmodgeQ
	  * Postgresql tsVector column native functions: https://dzone.com/articles/postgres-full-text-search-with-hibernate-6
	  */
	private static void performFullTextSearchTsVector(Winedocs2Repo winedocs2Repo,String argValue) {
		
		System.out.println("performFullTextSearchTsVector Start");
		List<Winedocs2> winedocs2List = winedocs2Repo.findFullTextSearch(argValue);
		System.out.println("performFullTextSearchTsVector Complete, Size is: " + winedocs2List.size());
		
		
		for(Winedocs2 w: winedocs2List) {
			System.out.println("TSVector FullText Search Results are: id= " + 
					w.getId() + ", Description= " + w.getDescription());
		}
	}

	
	/*
	 * Semantic search: https://www.baeldung.com/spring-ai-pgvector-semantic-search
	 * Semantic vs FullText Search: https://www.tigerdata.com/blog/combining-semantic-search-and-full-text-search-in-postgresql-with-cohere-pgvector-and-pgai#understanding-full-text-search-and-semantic-search
	 * RAG Search: https://www.sohamkamani.com/java/spring-ai-rag-application/
	 * Cosine Distance vs Cosine similarity: https://brightinventions.pl/blog/gentle-intro-to-spring-ai-embedding-model-abstraction/
	 * */	
	private static void performSimilaritySearch(VectorStore vectorStore, String argValue) {
		
		String queryStr = argValue; //"2013"; //"0.0103715155,0.00011508846";
		
		/*
		 * vectorStore.similaritySearch(SearchRequest.builder() .query(queryStr)
		 * .topK(3) .build()) .forEach(result -> {
		 * System.out.println("Similarity Search Result: " + result.getText() +
		 * ", Score: " + result.getScore()); });
		 */
		
		System.out.println("Before similaritySearch ");
		
		List<Document> documentList = vectorStore.similaritySearch(SearchRequest.builder()
				.query(queryStr)
				.topK(3)
				.build());
		System.out.println("After similaritySearch ");
		
		List<String> strList = documentList.stream()
				.map(Document::getText)
				.toList();
		System.out.println("StrList size from vectorStore: " + strList.size());
		
		int count = 1;
		for(String s: strList) {
			System.out.println("StrList details, count= " + count + " : " + s);
			count++;
		}
		
	}
	
	/*Filters extactly 1 record matching the full title and not partial title value*/
	private static void performSimilaritySearchWithFilter(VectorStore vectorStore, String argValue) {
		
		String queryStr = argValue; 		
		String metaQuery = "Rainstorm 2013 Pinot";//"Rainstorm 2013 Pinot Gris (Willamette Valley)"; //"Trimbach 2012 Gewurztraminer (Alsace)";
		System.out.println("Before performSimilaritySearchWithFilter ");
		
		List<Document> documentList = vectorStore.similaritySearch(SearchRequest.builder()
				.query(queryStr)
				.topK(3)
				.filterExpression("title =='" + metaQuery + "'")
				.build());
		System.out.println("After performSimilaritySearchWithFilter ");
		
		List<String> strList = documentList.stream()
				.map(Document::getText)
				.toList();
		System.out.println("StrList size from vectorStore: " + strList.size());
		
		int count = 1;
		for(String s: strList) {
			System.out.println("StrList details, count= " + count + " : " + s);
			count++;
		}
	
	}
}
