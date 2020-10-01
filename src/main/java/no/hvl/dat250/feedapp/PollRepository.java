package no.hvl.dat250.feedapp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PollRepository extends CrudRepository<Poll, Long> {
	

}
