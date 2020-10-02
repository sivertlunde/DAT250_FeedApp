package no.hvl.dat250.feedapp.repository;
import org.springframework.data.repository.CrudRepository;

import no.hvl.dat250.feedapp.model.Poll;

public interface PollRepository extends CrudRepository<Poll, Long> {

	Iterable<Poll> findByTitleContaining(String title);
	

}
