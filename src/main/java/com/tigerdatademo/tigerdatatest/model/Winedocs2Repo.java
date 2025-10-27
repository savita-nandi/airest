package com.tigerdatademo.tigerdatatest.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Winedocs2Repo extends JpaRepository<Winedocs2, Long>{
	
	//phraseto_tsquery: Searches for complete Phrase:@Query(value="SELECT * FROM winedocs_2 w WHERE w.desctitle @@ phraseto_tsquery(:searchText)", nativeQuery = true)
	/*to_tsquery: Allows usage of &,|,!,<->,[followed by]: SearchString='Aromas <-> include <-> tropical <-> fruit' OR 'Aromas & include & tropical & fruit'. \
	 * Without these operators, the application gives an exception
	 *@Query(value="SELECT * FROM winedocs_2 w WHERE w.desctitle @@ to_tsquery(:searchText)", nativeQuery = true)
	*/
	/*plainto_tsquery: Searches by appending words with &
	*@Query(value="SELECT * FROM winedocs_2 w WHERE w.desctitle @@ to_tsquery('english',:searchText)", nativeQuery = true)
	*/
	@Query(value="SELECT * FROM winedocs_2 w WHERE w.desctitle @@ plainto_tsquery('english',:searchText)", nativeQuery = true)
	public List<Winedocs2> findFullTextSearch(@Param("searchText") String searchText);
	
	
	
}
