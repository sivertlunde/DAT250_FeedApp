package no.hvl.dat250.feedapp.repository;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import no.hvl.dat250.feedapp.model.Poll;

public interface PollRepository extends CrudRepository<Poll, Long> {

	Iterable<Poll> findByTitleContaining(String title);
	
	@Query("from Poll p where p.endDate < CURRENT_TIMESTAMP and p.endDate > :prev")//TODO: DETTE ER BROKEN 
	Optional<Integer> findEndedPolls(@Param("prev")Timestamp prev);

}
