package com.restapi.repository;

import com.restapi.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQuery;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select u from Event u where u.category.id=?1")
    List<Event> findCategory(Long id);

    @Query(value = "SELECT e FROM Event e ORDER BY e.soldTickets DESC")
    List<Event> findAllTopEvents();

    @Query("SELECT e FROM Event e " +
            "JOIN Category c ON c.id = e.category.id " +
            "WHERE e.date BETWEEN :fromDate AND :toDate " +
            "AND e.price BETWEEN :minPrice AND :maxPrice " +
            "AND c.id IN :categoryList " +
            "AND e.id IN :venueList"
    )
    List<Event> findFilteredEvents(LocalDate fromDate, LocalDate toDate, int minPrice, int maxPrice,List<Long> categoryList,List<Long> venueList);

    @Query("SELECT e FROM Event e " +
            "INNER JOIN Category c ON c.id = e.category.id " +
            "WHERE e.date BETWEEN :fromDate AND :toDate " +
            "AND e.price BETWEEN :minPrice AND :maxPrice " +
            "AND c.id IN :categoryList "
    )
    List<Event> findFilteredEventsWithoutVenues(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice,
            @Param("categoryList") List<Long> categoryList
    );

    List<Event> findByDateBetweenAndPriceBetweenAndIdIn(
            LocalDate startDate, LocalDate endDate,
            double minPrice, double maxPrice,
            List<Long> eventIds
    );

    List<Event> findByDateBetweenAndPriceBetween(LocalDate fromDate, LocalDate toDate, int minPrice, int maxPrice);
}
