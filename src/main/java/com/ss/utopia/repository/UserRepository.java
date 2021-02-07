package com.ss.utopia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ss.utopia.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query(value="SELECT * FROM user WHERE role_id = ?1", nativeQuery=true)
	List<User> findAllByRoleId(int id);
	
	@Query(value="SELECT * FROM user WHERE role_id = ?1 AND id = ?2", nativeQuery=true)
	User findByRoleId(Integer roleId , Integer userId);
	
	@Query(value="SELECT * FROM user WHERE email = ?1", nativeQuery=true)
	User findByEmail(String email);
	
	@Query(value="DELETE FROM user WHERE id =1", nativeQuery=true)
	User deleteById(String email);
	
	
}