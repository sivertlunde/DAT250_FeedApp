package no.hvl.dat250.feedapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.User;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.repository.UserRepository;
import no.hvl.dat250.feedapp.repository.VoteRepository;
import no.hvl.dat250.feedapp.service.FirebaseInitializer;

@CrossOrigin(origins = "*")
@RestController
public class VoteController {
	@Autowired
	VoteRepository voteRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PollRepository pollRepository;
	@Autowired
	FirebaseInitializer firebase;

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
	public ResponseEntity<Vote> createVote(@RequestParam(required = false) Long voterId,
			@RequestParam(required = true) Long pollId, @RequestParam(required = true) Integer vote,
			@RequestHeader(name = "Authorization", required = true) String token) {
		try {
			Vote newVote = new Vote(vote, null, null);
			System.out.println(voterId);
			System.out.println(pollId);
			System.out.println(token);
			if (voterId != null) {
				String _token = token.replaceAll("Bearer ", "");
				if (tokenIsValid(_token)) {
					Optional<User> voterById = userRepository.findById(voterId);
					if (voterById.isPresent()) {
						System.out.println("heiii");
						newVote.setVoter(voterById.get());
						System.out.println(voterById.get().getEmail());
					} else {
						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
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

	@PostMapping("/votes/many")
	public ResponseEntity<List<Vote>> createVotes(@RequestParam(required = true) String poll,
			@RequestParam(required = false) String red, @RequestParam(required = false) String green) {
		try {
			if (poll != null) {
				int redVotes;
				int greenVotes;
				Long pollId;
				try {
					pollId = Long.valueOf(poll);
					redVotes = red != null ? Integer.parseInt(red) : 0;
					greenVotes = green != null ? Integer.parseInt(green) : 0;
				} catch (NumberFormatException e) {
					return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
				}

				Optional<Poll> pollById = pollRepository.findById(pollId);
				if (pollById.isPresent()) {
					List<Vote> votes = new ArrayList<>();
					for (int i = 0; i < redVotes; i++) {
						Vote v = new Vote(0, null, pollById.get());
						voteRepository.save(v);
						votes.add(v);
					}
					for (int i = 0; i < greenVotes; i++) {
						Vote v = new Vote(1, null, pollById.get());
						voteRepository.save(v);
						votes.add(v);
					}
					return new ResponseEntity<>(votes, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}

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

	private boolean tokenIsValid(String token) {
		try {
			FirebaseToken decodedToken = firebase.getAuth().verifyIdToken(token);
			String uid = decodedToken.getUid();
			System.out.println("Returnert fra verifyToken: " + uid);
			return true;
		} catch (FirebaseAuthException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
