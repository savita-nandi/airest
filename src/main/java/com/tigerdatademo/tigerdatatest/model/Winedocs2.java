package com.tigerdatademo.tigerdatatest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="winedocs_2")
public class Winedocs2 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;
	private String title;
	
	@Column(columnDefinition = "tsvector")
	private String desctitle ;
	
	@Column(columnDefinition = "vector(1536)")
	private float[] embedding;

	public Winedocs2() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Winedocs2(Long id, String description, String title, String desctitle, float[] embedding) {
		super();
		this.id = id;
		this.description = description;
		this.title = title;
		this.desctitle = desctitle;
		this.embedding = embedding;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesctitle() {
		return desctitle;
	}

	public void setDesctitle(String desctitle) {
		this.desctitle = desctitle;
	}

	public float[] getEmbedding() {
		return embedding;
	}

	public void setEmbedding(float[] embedding) {
		this.embedding = embedding;
	}
	
	
	
}
