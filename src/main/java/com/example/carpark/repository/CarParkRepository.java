package com.example.carpark.repository;

import com.example.carpark.model.CarParkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarParkRepository extends JpaRepository<CarParkEntity, Long> {
    @Query("SELECT c FROM CarParkEntity c JOIN c.availability a WHERE a.availableLots > 0 ORDER BY " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(c.latitude)) * " +
            "cos(radians(c.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
            "sin(radians(c.latitude)))) ASC")
    Page<CarParkEntity> findAvailableCarParks(@Param("latitude") double latitude,
                                              @Param("longitude") double longitude,
                                              Pageable pageable);

    Optional<CarParkEntity> findByCarParkNumber(String carParkNumber);

}
