package com.tigerdatademo.tigerdatatest.entity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WinedocsRepo extends JpaRepository<Winedocs, UUID>{

	//@Query("SELECT w FROM Winedocs w WHERE w.embedding IS NULL order by w.id ASC")
	@Query("SELECT w FROM Winedocs w")
	public List<Winedocs> findWineDocsNotEmbedded();
	
	
	
}
