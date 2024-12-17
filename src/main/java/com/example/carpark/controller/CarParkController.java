package com.example.carpark.controller;

import com.example.carpark.dto.CarParkDto;
import com.example.carpark.service.CarParkService;
import com.example.carpark.task.CarParkAvailabilityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/carparks")
public class CarParkController {
    @Autowired
    private CarParkService carParkService;

    @Autowired
    private CarParkAvailabilityUpdater carParkAvailabilityUpdater;

    @GetMapping("/nearest")
    public ResponseEntity<List<CarParkDto>> getNearestCarParks(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam int page,
            @RequestParam int perPage) {
        if (latitude == 0 || longitude == 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(carParkService.findNearestCarParks(latitude, longitude, page, perPage));
    }

    @RequestMapping("/updateAvailability")
    public String updateAvailability() {
        carParkAvailabilityUpdater.updateAvailability();
        return "Car park availability updated successfully!";
    }

}
