package no.hvl.dat250.feedapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FeedappApplication {

	private static final Logger log = LoggerFactory.getLogger(FeedappApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FeedappApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository ur) {
		return (args) -> {
			// save a few entities
//			Role admin = new Role("Admin");
//			Role registeredUser = new Role("Registered user");
			User user1 = new User("username1", "password1", null);
			User user2 = new User("username2", "password2", null);
//			Poll poll1 = new Poll("First poll", "To test the stuff", "It works", "It doesn't work", true, user1);
//			Vote vote1 = new Vote(1, user2, poll1);
			
//			rr.save(admin);
//			rr.save(registeredUser);
//			ur.save(user1);
//			ur.save(user2);
//			pr.save(poll1);
//			vr.save(vote1);

			// fetch all customers
			log.info("Users found with findAll():");
			log.info("-------------------------------");
			for (User user : ur.findAll()) {
				log.info(user.getUsername());
			}
			log.info("");

			// fetch user by username
			log.info("User found with findByUsername('username1'):");
			log.info("--------------------------------------------");
			User found = ur.findByUsername("username1");
			log.info(found.getUsername() + " " + found.getPassword());
			// for (Customer bauer : repository.findByLastName("Bauer")) {
			// log.info(bauer.toString());
			// }
			log.info("");
		};
	}

}
