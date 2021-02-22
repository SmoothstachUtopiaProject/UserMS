package com.ss.utopia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ss.utopia.model.UserToken;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, String> {

}
