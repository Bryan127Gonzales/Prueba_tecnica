package com.armadillo.coworking.repository;

import com.armadillo.coworking.model.entity.Booking;
import com.armadillo.coworking.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking,Long> {
    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.room.id = :roomId
        AND b.startTime < :end
        AND b.endTime > :start
    """)
    boolean existsOverlappingBooking(
            Long roomId,
            LocalDateTime start,
            LocalDateTime end
    );
}
