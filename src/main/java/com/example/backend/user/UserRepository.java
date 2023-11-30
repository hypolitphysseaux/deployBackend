package com.example.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Deprecated
    @Query("SELECT s FROM User s WHERE s.email=?1")
    Optional<User> findUserByEmail(String email);

    @Deprecated
    @Modifying
    @Query("DELETE FROM User s WHERE s.email=?1")
    Optional<User> deleteUserBymail(String email);

    @Deprecated
    @Query("DELETE FROM User s WHERE s.email=?1")
    void deleteUserByEmail(Optional<User> userByEmail);
    // Find a user that contains a substring
    List<User> findUserByEmailContainingOrMenoContainingOrPriezviskoContaining(String email, String meno, String priezvisko);
}
