package com.selfshelf.project.repository;

import com.selfshelf.project.model.Reservation;
import com.selfshelf.project.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findReservationsByBookEntityIdEqualsOrderByReservationStartDateDesc(Long bookId);

    @Query("Select r " + "FROM Reservation r " + "Where r.status = :#{#status} ")
    List<Reservation> findActiveReservations(Status status);

    List<Reservation> findReservationsByReservationEndDateBefore(LocalDate date);

    @Query(
            "Select r " +
                    "FROM Reservation r " +
                    "JOIN r.userEntity user " +
                    "JOIN r.bookEntity book " +
                    "Where user.id = :#{#userId} " +
                    "And book.id=:#{#bookId} "
    )
    List<Reservation> findReservationsByBookIdEqualsAndUserIdEquals(Long bookId, Long userId);

}
