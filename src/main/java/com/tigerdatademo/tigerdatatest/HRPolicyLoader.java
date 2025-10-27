package com.tigerdatademo.tigerdatatest;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


@Component
public class HRPolicyLoader {
	
	private final VectorStore vectorStore;
	
	@Value("classpath:Eazybytes_HR_Policies.pdf")
	private Resource hrPolicyFile;
	
	public HRPolicyLoader(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	/*The @PostConstruct annotation in Spring marks a method that should be executed after a 
	 *Spring bean has been fully initialized and all its dependencies have been injected.
	 *Commented to avoid duplicate records in vector_store table*/
	
	//@PostConstruct
	public void loadHrPolicyPDF() {
		TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(hrPolicyFile);
		List<Document> documentList = tikaDocumentReader.get();
		
		/*Chunks size implies Tokens/Words as per AIs token concept*/
		TextSplitter textSplitter = 
				TokenTextSplitter.builder().withChunkSize(100).withMaxNumChunks(400).build();
		
		System.out.println("loadHrPolicyPDF()->Before adding splitted HR Policy docs of size:" + documentList.size());
		//vectorStore.add(textSplitter.split(documentList));
		System.out.println("loadHrPolicyPDF()->Completed adding HR Policy docs");
		
		
	}
}

