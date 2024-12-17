package com.example.carpark.service;
import com.example.carpark.dto.CarParkDto;
import com.example.carpark.model.CarParkEntity;
import com.example.carpark.repository.CarParkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarParkService {

    @Autowired
    private CarParkRepository carParkRepository;

    public List<CarParkDto> findNearestCarParks(double latitude, double longitude, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return carParkRepository.findAvailableCarParks(latitude, longitude, pageable)
                .stream()
                .map(this::toCarParkDto)
                .collect(Collectors.toList());
    }

    private CarParkDto toCarParkDto(CarParkEntity entity) {
        return new CarParkDto(entity.getAddress(), entity.getLatitude(), entity.getLongitude(),
                entity.getTotalLots(), entity.getAvailableLots());
    }

}
