package com.ss.utopia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ss.utopia.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query(value="SELECT * FROM user WHERE role_id = ?1", nativeQuery=true)
	List<User> findByRoleId(Integer id);
	
	@Query(value="SELECT * FROM user WHERE email = ?1", nativeQuery=true)
	Optional<User> findByEmail(String email);
}