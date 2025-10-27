package com.tigerdatademo.tigerdatatest.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VectorstoreRepo extends JpaRepository<VectorstoreTbl, UUID>{
	
	
	  @Query("SELECT v FROM VectorstoreTbl v WHERE content LIKE %:queryValue%")
	  public List<VectorstoreTbl> findByFullTextSearch(@Param("queryValue") String
	  queryValue);
	 
	
	
	@Query("SELECT v FROM VectorstoreTbl v WHERE v.embedding IS NULL order by v.id ASC")
	public List<VectorstoreTbl> findDocsNotEmbedded();
}
