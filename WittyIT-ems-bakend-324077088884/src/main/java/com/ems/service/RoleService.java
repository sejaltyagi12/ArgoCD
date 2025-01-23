package com.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.domain.Role;
import com.ems.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	/**
	 * Find by id.
	 *
	 * @param id
	 * 
	 * @return the Role
	 */
	public Role findById(Integer id) {
		return roleRepository.findById(id);
	}

	/**
	 * Fetch all roles.
	 *
	 * 
	 * @return the Roles
	 */
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}

}
