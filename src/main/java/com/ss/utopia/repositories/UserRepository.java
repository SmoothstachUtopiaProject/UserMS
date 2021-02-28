package com.ss.utopia.repositories;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ss.utopia.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query(value = "SELECT * FROM user WHERE role_id = ?1", nativeQuery = true)
  List<User> findByRoleId(Integer id);

  @Query(value = "SELECT * FROM user WHERE email = ?1", nativeQuery = true)
  Optional<User> findByEmail(String email);

  @Query(value = "SELECT * FROM user WHERE phone = ?1", nativeQuery = true)
  Optional<User> findByPhone(String phone);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM user WHERE id = ?1", nativeQuery = true)
  void deleteById(Integer id);

}