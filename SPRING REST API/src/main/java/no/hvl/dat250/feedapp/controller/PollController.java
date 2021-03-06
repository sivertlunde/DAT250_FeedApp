package no.hvl.dat250.feedapp.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
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
import no.hvl.dat250.feedapp.service.FirebaseInitializer;

@CrossOrigin(origins = "*")
@RestController
public class PollController {

	@Autowired
	PollRepository pollRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	FirebaseInitializer firebase;

	@GetMapping("/polls")
	public ResponseEntity<List<Poll>> getAllPolls(@RequestParam(required = false) String title) {
		try {
			List<Poll> polls = new ArrayList<Poll>();

			if (title == null) {
				pollRepository.findAll().forEach(polls::add);
			} else {
				pollRepository.findByTitleContaining(title).forEach(polls::add);
			}

			if (polls.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(polls, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/polls/{id}")
	public ResponseEntity<Poll> getPollById(@PathVariable("id") long id) {
		Optional<Poll> pollData = pollRepository.findById(id);

		if (pollData.isPresent()) {
			return new ResponseEntity<>(pollData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/polls/result/{id}")
	public ResponseEntity<Map<String, Object>> getLiveResults(@PathVariable("id") long id) {
		Optional<Poll> pollData = pollRepository.findById(id);
		if (pollData.isPresent()) {
			
			Poll poll = pollData.get();
			int red = 0;
			int green = 0;

			List<Vote> votes = poll.getVotes();
			for (Vote v : votes) {
				int result = v.getResult();
				if (result == 0) {
					red++;
				} else {
					green++;
				}
			}
			
			JSONObject redJson = new JSONObject();
			redJson.put("text", poll.getRed());
			redJson.put("amount", red);
			
			JSONObject greenJson = new JSONObject();
			greenJson.put("text", poll.getGreen());
			greenJson.put("amount", green);
			
			JSONObject json = new JSONObject();
			json.put("id", poll.getId());
			json.put("title", poll.getTitle());
			json.put("description", poll.getDescription());
			json.put("startDate", poll.getStartDate());
			json.put("endDate", poll.getEndDate());
			json.put("red", redJson);
			json.put("green", greenJson);
			return new ResponseEntity<>(json.toMap(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}	
	}

	@GetMapping("/polls/recentlyfinished")
	public ResponseEntity<List<Long>> getEndedPolls() {
		Timestamp prev = new Timestamp(System.currentTimeMillis()-60000);
		Optional<List<Long>> idEndedPolls = pollRepository.findEndedPolls(prev);
		
		if (idEndedPolls.isPresent()) {
			return new ResponseEntity<>(idEndedPolls.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@PostMapping("/polls")
	// @RequestMapping(value = "/polls", method = RequestMethod.POST,
	// produces = "charset=utf-8; application/json")
	// @ResponseBody
	public ResponseEntity<Poll> createPoll(@RequestHeader (name="Authorization") String token, @RequestBody Poll poll) {
		String _token = token.replaceAll("Bearer ", "");
		FirebaseToken firebaseToken = getValidToken(_token);
		if (firebaseToken != null) {
			try {
				Poll newPoll = new Poll(poll.getTitle(), poll.getDescription(), poll.getGreen(), poll.getRed(),
						poll.getIsPublic(), null);
				if(poll.getStartDate() != null) {
					Date now = new Date(System.currentTimeMillis());
					newPoll.setStartDate(now);
					if (poll.getEndDate() != null) {
						Date endDate = poll.getEndDate();
						newPoll.setEndDate(endDate);
					}
				}
				
				if (firebaseToken.getUid() != null) {
					Optional<User> user = userRepository.findById(firebaseToken.getUid());
					if (user.isPresent()) {
						newPoll.setCreatedBy(user.get());
						Poll savedPoll = pollRepository.save(newPoll);
						return new ResponseEntity<>(savedPoll, HttpStatus.CREATED);
					} else {
						return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
					}

				}
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		
	}

	@PutMapping("/polls")
	public ResponseEntity<Poll> updatePoll(@RequestHeader (name="Authorization") String token, @RequestParam(required = true) Long pollId, @RequestBody Poll poll) {
		System.out.println(poll);
		String _token = token.replaceAll("Bearer ", "");
		FirebaseToken firebaseToken = getValidToken(_token);
		if (firebaseToken != null) {
			Optional<Poll> pollData = pollRepository.findById(pollId);
			if (pollData.isPresent()) {
				Poll _poll = pollData.get();
				boolean userIsCreator = _poll.getCreatedBy().getId().equals(firebaseToken.getUid());
				if (userIsCreator) {
					_poll.setTitle(poll.getTitle());
					_poll.setDescription(poll.getDescription());
					_poll.setGreen(poll.getGreen());
					_poll.setRed(poll.getRed());
					_poll.setIsPublic(poll.getIsPublic());
					_poll.setStartDate(poll.getStartDate());
					_poll.setEndDate(poll.getEndDate());
					return new ResponseEntity<>(pollRepository.save(_poll), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} 
		} else {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PutMapping("/polls/start/{id}")
	public ResponseEntity<Poll> startPoll(@PathVariable("id") long id, @RequestParam(required=false) Long millis){
		Optional<Poll> pollData = pollRepository.findById(id);
		Date date = new Date(System.currentTimeMillis());
		if(pollData.isPresent()) {
			Poll _poll = pollData.get();
			_poll.setStartDate(date);
			if(millis != null) {
			Date enddate = new Date(System.currentTimeMillis()+millis);
			_poll.setEndDate(enddate);
			}
			return new ResponseEntity<>(pollRepository.save(_poll), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/polls/{id}")
	public ResponseEntity<HttpStatus> deletePoll(@PathVariable("id") long id) {
		try {
			pollRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/polls")
	public ResponseEntity<HttpStatus> deleteAllPolls() {
		try {
			pollRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private boolean userIsAdmin(String uid) {
		Optional<User> user = userRepository.findById(uid);
		if (user.isPresent()) {
			User _user = user.get();
			return _user.getRole().getRole().equalsIgnoreCase("admin");
		}
		return false;
	}
	
	private FirebaseToken getValidToken(String token) {
		try {
			return firebase.getAuth().verifyIdToken(token);
		} catch (FirebaseAuthException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
