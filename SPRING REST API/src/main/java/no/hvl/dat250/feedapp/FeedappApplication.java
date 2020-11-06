package no.hvl.dat250.feedapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.User;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.repository.RoleRepository;
import no.hvl.dat250.feedapp.repository.UserRepository;
import no.hvl.dat250.feedapp.repository.VoteRepository;

@SpringBootApplication
public class FeedappApplication {

	private static final Logger log = LoggerFactory.getLogger(FeedappApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FeedappApplication.class, args);
	}

	/*
	@Bean
	public CommandLineRunner demo(RoleRepository rr, UserRepository ur, PollRepository pr, VoteRepository vr) {
		return (args) -> {
			// save a few entities
			Role admin = new Role("Admin");
			Role registeredUser = new Role("Registered user");
			User user1 = new User("username1", "password1");
			User user2 = new User("username2", "password2");
			Poll poll1 = new Poll("First poll", "To test the stuff", "It works", "It doesn't work", true, user1);
			Vote vote1 = new Vote(1, user2, poll1);
			Vote vote2 = new Vote(1, user1, poll1);
			
//			rr.save(admin);
//			rr.save(registeredUser);
//			ur.save(user1);
//			ur.save(user2);
//			pr.save(poll1);
//			vr.save(vote1);
//			vr.save(vote2);

			// fetch all polls
			log.info("Polls found with findAll():");
			log.info("-------------------------------");
			for (Poll poll : pr.findAll()) {
				log.info(poll.toString());
				log.info("With votes:");
				for (Vote vote : vr.findByPoll(poll)) {
					log.info(vote.toString());
				}
			}
			log.info("");
		};
	}
	*/

}