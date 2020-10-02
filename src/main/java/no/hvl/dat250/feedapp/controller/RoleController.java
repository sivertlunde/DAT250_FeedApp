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

import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.repository.RoleRepository;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class RoleController {
	
	@Autowired
	RoleRepository roleRepository;
	
	@GetMapping("/roles")
	public ResponseEntity<List<Role>> getAllRoles(@RequestParam(required = false) String role){
		try {
			List<Role> roles = new ArrayList<Role>();
			
			if(role == null) {
				roleRepository.findAll().forEach(roles::add);
			}else {
				roleRepository.findByRoleContaining(role).forEach(roles::add);
			}if(roles.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(roles, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/roles/{id}")
	public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id){
		Optional<Role> roleData = roleRepository.findById(id);
		if(roleData.isPresent()) {
			return new ResponseEntity<>(roleData.get(), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/roles")
	public ResponseEntity<Role> createRole(@RequestBody Role role){
		try {
			Role newRole = roleRepository.save(new Role(role.getRole()));
			return new ResponseEntity<>(newRole, HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/roles/{id}")
	public ResponseEntity<Role> updateRole(@PathVariable("id") Long id, @RequestBody Role role){
		Optional<Role> roleData = roleRepository.findById(id);
		if(roleData.isPresent()) {
			Role editRole = roleData.get();
			editRole.setId(role.getId());
			editRole.setRole(role.getRole());
			return new ResponseEntity<>(roleRepository.save(editRole), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/roles/{id}")
	public ResponseEntity<HttpStatus> deleteRole(@PathVariable("id") Long id) {
	    try {
	        roleRepository.deleteById(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}
