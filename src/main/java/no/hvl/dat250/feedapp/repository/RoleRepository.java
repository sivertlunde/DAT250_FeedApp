package no.hvl.dat250.feedapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import no.hvl.dat250.feedapp.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	List<Role> findByRoleContaining(String role);
}