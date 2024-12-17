package com.example.carpark.repository;

import com.example.carpark.model.AvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity, Long> {
    // Custom query to find AvailabilityEntity by CarParkEntity ID
    Optional<AvailabilityEntity> findByCarParkId(Long carParkId);
}
