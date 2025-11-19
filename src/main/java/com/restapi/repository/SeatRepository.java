package com.restapi.repository;

import com.restapi.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
    
    @Transactional
    @Modifying
    @Query("delete from Seat s where s.order.id=:id")
    void deleteSeats(@Param("id")long id);

}
