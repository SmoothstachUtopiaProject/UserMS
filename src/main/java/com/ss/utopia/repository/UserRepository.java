package com.ss.utopia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ss.utopia.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}