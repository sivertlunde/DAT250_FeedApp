package no.hvl.dat250.feedapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Vote;

public interface VoteRepository extends CrudRepository<Vote, Long> {
	List<Vote> findByPoll(Poll poll);
}