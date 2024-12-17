package com.example.carpark.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CarParkDataLoaderRunner implements CommandLineRunner {

    private final CarParkDataLoader carParkDataLoader;

    public CarParkDataLoaderRunner(CarParkDataLoader carParkDataLoader) {
        this.carParkDataLoader = carParkDataLoader;
    }

    @Override
    public void run(String... args) throws IOException {
        String filePath = "src/main/resources/static/HDBCarparkInformation.csv";
        carParkDataLoader.parseCsvToCarParks(filePath);
        //carParkDataLoader.loadCarParkData(filePath);
        System.out.println("Car park data loaded successfully!");
    }
}

