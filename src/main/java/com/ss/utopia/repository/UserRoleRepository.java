package com.ss.utopia.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ss.utopia.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

	@Query(value = "SELECT * FROM user_role WHERE id = ?1", nativeQuery = true)
	public UserRole findUserRoleById(int id);

}
