package no.hvl.dat250.feedapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.User;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.repository.UserRepository;
import no.hvl.dat250.feedapp.repository.VoteRepository;

@RestController
public class VoteController {
	@Autowired
	VoteRepository voteRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PollRepository pollRepository;

	@GetMapping("/votes")
	public ResponseEntity<List<Vote>> getAllVotes(@RequestParam(required = false) String title) {
		try {
			List<Vote> votes = new ArrayList<Vote>();

			voteRepository.findAll().forEach(votes::add);

			if (votes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(votes, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/votes/{id}")
	public ResponseEntity<Vote> getVoteById(@PathVariable("id") long id) {
		Optional<Vote> voteData = voteRepository.findById(id);

		if (voteData.isPresent()) {
			return new ResponseEntity<>(voteData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/votes")
	public ResponseEntity<Vote> createVote(@RequestBody Vote vote, @RequestParam(required = false) Long voterId,
			@RequestParam(required = true) Long pollId) {
		try {
			Vote newVote = new Vote(vote.getResult(), null, null);
			if (voterId != null) {
				Optional<User> voterById = userRepository.findById(voterId);
				if (voterById.isPresent()) {
					newVote.setVoter(voterById.get());
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			}

			if (pollId != null) {
				Optional<Poll> pollById = pollRepository.findById(pollId);
				if (pollById.isPresent()) {
					newVote.setPoll(pollById.get());					
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
			}
			Vote _vote = voteRepository.save(newVote);
			return new ResponseEntity<>(_vote, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/votes/{id}")
	public ResponseEntity<Vote> updateVote(@PathVariable("id") Long id, @RequestBody Vote vote) {
		Optional<Vote> voteData = voteRepository.findById(id);

		if (voteData.isPresent()) {
			Vote _vote = voteData.get();
			_vote.setResult(vote.getResult());
			return new ResponseEntity<>(voteRepository.save(_vote), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/votes/{id}")
	public ResponseEntity<HttpStatus> deleteVote(@PathVariable("id") Long id) {
		try {
			voteRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/votes")
	public ResponseEntity<HttpStatus> deleteAllVotes() {
		try {
			voteRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
