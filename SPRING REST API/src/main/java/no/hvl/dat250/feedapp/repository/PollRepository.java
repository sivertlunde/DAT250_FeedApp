package no.hvl.dat250.feedapp.repository;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import no.hvl.dat250.feedapp.model.Poll;

public interface PollRepository extends CrudRepository<Poll, Long> {

	Iterable<Poll> findByTitleContaining(String title);
	
	@Query("from Poll p where p.endDate < CURRENT_TIMESTAMP")//TODO: DETTE ER BROKEN
	Optional<Poll> findEndedPolls();

}
