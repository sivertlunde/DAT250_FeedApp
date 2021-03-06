package no.hvl.dat250.feedapp.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import no.hvl.dat250.feedapp.model.User;

public interface UserRepository extends CrudRepository<User, String> {
	Optional<User> findByEmail(String email);
}
