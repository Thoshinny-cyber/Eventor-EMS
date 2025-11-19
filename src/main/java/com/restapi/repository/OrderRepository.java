package com.restapi.repository;

import com.restapi.model.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    // CORRECT: Fetch seats + event in one query
    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.seat " +
            "LEFT JOIN FETCH o.event " +
            "WHERE o.users.id = :userId")
    List<Order> findAllForId(@Param("userId") Long userId);
}
