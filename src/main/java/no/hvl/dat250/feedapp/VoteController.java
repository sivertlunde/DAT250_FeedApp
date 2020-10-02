package no.hvl.dat250.feedapp;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {
	@Autowired
	VoteRepository voteRepository;

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
	public ResponseEntity<Vote> createVote(@RequestBody Vote vote) {
		try {
			Vote _vote = voteRepository.save(new Vote(vote.getResult(), vote.getVoter(), vote.getPoll()));
			return new ResponseEntity<>(_vote, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/votes/{id}")
	public ResponseEntity<Vote> updateVote(@PathVariable("id") long id, @RequestBody Vote vote) {
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
	public ResponseEntity<HttpStatus> deleteVote(@PathVariable("id") long id) {
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
