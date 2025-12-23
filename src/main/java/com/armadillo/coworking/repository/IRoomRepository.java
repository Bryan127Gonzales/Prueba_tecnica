package com.armadillo.coworking.repository;

import com.armadillo.coworking.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoomRepository extends JpaRepository<Room,Long> {

}
