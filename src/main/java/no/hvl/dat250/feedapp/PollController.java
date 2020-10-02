package no.hvl.dat250.feedapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PollController {
	
	@Autowired
	PollRepository pollrepository;

	@GetMapping("/polls")
	public ResponseEntity<List<Poll>> getAllPolls(@RequestParam(required=false)String title) {
		try {
			List<Poll> polls = new ArrayList<Poll>();
			
			if(title==null) {
				pollrepository.findAll().forEach(polls::add);
			}else {
				pollrepository.findByTitleContaining(title).forEach(polls::add);
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
	    Optional<Poll> pollData = pollrepository.findById(id);

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
	  public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
	    try {
	      Poll _poll = pollrepository
	          .save(new Poll(
	        		  poll.getTitle(),
	        		  poll.getDescription(),
	        		  poll.getGreen(),
	        		  poll.getRed(),
	        		  poll.getIsPublic(),
	        		  poll.getCreatedBy()
	        		  ));
	      return new ResponseEntity<>(_poll, HttpStatus.CREATED);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	
	@PutMapping("/polls/{id}")
	  public ResponseEntity<Poll> updatePoll(@PathVariable("id") long id, @RequestBody Poll poll) {
	    Optional<Poll> pollData = pollrepository.findById(id);

	    if (pollData.isPresent()) {
	      Poll _poll = pollData.get();
	      _poll.setTitle(poll.getTitle());
	      _poll.setDescription(poll.getDescription());
	      _poll.setGreen(poll.getGreen());
	      _poll.setRed(poll.getRed());
	      _poll.setIsPublic(poll.getIsPublic());
	      _poll.setCreatedBy(poll.getCreatedBy());
	      return new ResponseEntity<>(pollrepository.save(_poll), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }
	
	 @DeleteMapping("/polls/{id}")
	  public ResponseEntity<HttpStatus> deletePoll(@PathVariable("id") long id) {
	    try {
	      pollrepository.deleteById(id);
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	
	 @DeleteMapping("/polls")
	  public ResponseEntity<HttpStatus> deleteAllPolls() {
	    try {
	      pollrepository.deleteAll();
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	 }
	 
}