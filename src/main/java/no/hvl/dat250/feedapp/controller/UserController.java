package no.hvl.dat250.feedapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.User;
import no.hvl.dat250.feedapp.repository.RoleRepository;
import no.hvl.dat250.feedapp.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> users = new ArrayList<User>();
			userRepository.findAll().forEach(users::add);

			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(users, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
		Optional<User> user = userRepository.findById(id);

		System.out.println(id);
		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/users/username/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
		Optional<User> user = userRepository.findByUsername(username);

		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(@RequestBody User user, @RequestParam(required = true) Long roleId) {
		try {
			User newUser = new User(user.getUsername(), user.getPassword());
			if (roleId != null) {
				Optional<Role> role = roleRepository.findById(roleId);
				if (role.isPresent()) {
					newUser.setRole(role.get());
				}
			}
			User savedUser = userRepository.save(newUser);
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/users/{id}")
	//@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user, @RequestParam(required = false) Long roleId) {
		Optional<User> userData = userRepository.findById(id);

		if (userData.isPresent()) {
			User updatedUser = userData.get();
			if (user.getUsername() != null) {
				updatedUser.setUsername(user.getUsername());
			}
			if (user.getPassword() != null) {
				updatedUser.setPassword(user.getPassword());
			}
			if (roleId != null) {
				Optional<Role> role = roleRepository.findById(roleId);
				if (role.isPresent()) {
					updatedUser.setRole(role.get());
				}
			}
			return new ResponseEntity<>(userRepository.save(updatedUser), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
