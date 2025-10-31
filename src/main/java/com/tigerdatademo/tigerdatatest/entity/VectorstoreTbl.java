package com.tigerdatademo.tigerdatatest.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="vector_store")
public class VectorstoreTbl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UUID id; //predefined column
	private String category; //custom column
	private String title; //custom column
	private String description;	 //custom column
	
	private String content; //predefined column, hardcoded in pgVector jar
	private String metadata; //predefined column, hardcoded in pgVector jar	
	@Column(columnDefinition = "vector(1536)")
	private float[] embedding; //predefined column, hardcoded in pgVector jar

	
	public VectorstoreTbl() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public VectorstoreTbl(UUID id, String category, String title, String description, String content, String metadata,
			float[] embedding) {
		super();
		this.id = id;
		this.category = category;
		this.title = title;
		this.description = description;
		this.content = content;
		this.metadata = metadata;
		this.embedding = embedding;
	}


	public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getMetadata() {
		return metadata;
	}


	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}


	public float[] getEmbedding() {
		return embedding;
	}


	public void setEmbedding(float[] embedding) {
		this.embedding = embedding;
	}
	
	
	
}
