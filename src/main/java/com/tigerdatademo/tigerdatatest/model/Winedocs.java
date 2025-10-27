package com.tigerdatademo.tigerdatatest.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="winedocs")
public class Winedocs {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UUID id; //predefined column
	
	private String description;
	private String title;
	
	/*
	 * @Column(columnDefinition = "vector(1536)") private float[] embedding;
	 */

	public Winedocs() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//, float[] embedding
	/*
	 * public Winedocs(Long id, String description, String title) { super(); this.id
	 * = id; this.description = description; this.title = title; this.embedding =
	 * embedding; }
	 */
	
	public Winedocs(UUID id, String description, String title) {		
		this.id = id;
		this.description = description;
		this.title = title;
	}
	
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setContent(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * public float[] getEmbedding() { return embedding; }
	 * 
	 * public void setEmbedding(float[] embedding) { this.embedding = embedding; }
	 */
	
	
}
