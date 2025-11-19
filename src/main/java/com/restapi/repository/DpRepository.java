package com.restapi.repository;

import com.restapi.model.AppUser;
import com.restapi.model.UserDP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DpRepository extends JpaRepository<UserDP, Long> {

    @Query("select u from UserDP u where u.user.id=:id")
    UserDP findByUserId(long id);
}
