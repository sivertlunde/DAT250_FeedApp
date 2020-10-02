package no.hvl.dat250.feedapp;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
	List<Role> findByRoleContaining(String role);
}