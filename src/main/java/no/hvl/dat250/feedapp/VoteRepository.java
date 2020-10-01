package no.hvl.dat250.feedapp;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, Long> {
	List<Vote> findByPoll(Poll poll);
}