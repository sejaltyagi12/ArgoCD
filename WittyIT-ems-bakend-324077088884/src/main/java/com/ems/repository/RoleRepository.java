package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



import com.ems.domain.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
	
	
	/**
	 * Find by id
	 * @param id
	 * @return Role
	 */
	Role findById(Integer id);
	
	/**
	 * Find all roles
	 * @return Roles
	 */
	List<Role> findAll();
		

}
