package no.hvl.dat250.feedapp.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.User;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
public class PollController {
	
	@Autowired
	PollRepository pollRepository;
	
	@Autowired
	UserRepository userRepository;

	@GetMapping("/polls")
	public ResponseEntity<List<Poll>> getAllPolls(@RequestParam(required=false)String title) {
		try {
			List<Poll> polls = new ArrayList<Poll>();
			
			if(title==null) {
				pollRepository.findAll().forEach(polls::add);
			}else {
				pollRepository.findByTitleContaining(title).forEach(polls::add);
			}
			
			if(polls.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(polls, HttpStatus.OK);
		}catch(Exception e) {
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
	@PostMapping("/polls")
	//@RequestMapping(value = "/polls", method = RequestMethod.POST, 
			//produces = "charset=utf-8; application/json")
	//@ResponseBody
	  public ResponseEntity<Poll> createPoll(@RequestBody Poll poll, @RequestParam(required=true)Long userId) {
	    try {
	      Poll newPoll = new Poll(poll.getTitle(), poll.getDescription(), poll.getGreen(), poll.getRed(), poll.getIsPublic(), null);
	      if(userId != null) {
	    	  Optional<User> user = userRepository.findById(userId);
	    	  if(user.isPresent()) {
	    		  newPoll.setCreatedBy(user.get());
	    		  Poll savedPoll = pollRepository.save(newPoll);
	    		  return new ResponseEntity<>(savedPoll, HttpStatus.CREATED);
	    	  }else {
	    		  return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	    	  }
	    	  
	      }
	      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	    	e.printStackTrace();
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	
	@PutMapping("/polls/{id}")
	  public ResponseEntity<Poll> updatePoll(@PathVariable("id") long id, @RequestBody Poll poll) {
	    Optional<Poll> pollData = pollRepository.findById(id);

	    if (pollData.isPresent()) {
	      Poll _poll = pollData.get();
	      _poll.setTitle(poll.getTitle());
	      _poll.setDescription(poll.getDescription());
	      _poll.setGreen(poll.getGreen());
	      _poll.setRed(poll.getRed());
	      _poll.setIsPublic(poll.getIsPublic());
	      _poll.setCreatedBy(poll.getCreatedBy());
	      return new ResponseEntity<>(pollRepository.save(_poll), HttpStatus.OK);
	    } else {
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
	 
}
