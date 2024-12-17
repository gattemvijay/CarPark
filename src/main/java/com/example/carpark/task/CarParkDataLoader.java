package com.example.carpark.task;

import com.example.carpark.model.CarParkEntity;
import com.example.carpark.repository.CarParkRepository;
import com.example.carpark.util.CoordinateConverter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarParkDataLoader {

    private final CarParkRepository carParkRepository;

    @Autowired
    public CarParkDataLoader(CarParkRepository carParkRepository) {
        this.carParkRepository = carParkRepository;
    }


    public List<CarParkEntity> parseCsvToCarParks(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            List<CarParkEntity> carParks = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                String address = record.get("address");
                double northDistance = Double.parseDouble(record.get("y_coord"));
                double eastDistance = Double.parseDouble(record.get("x_coord"));
                int totalLots = Integer.parseInt(record.get("car_park_decks"));

                double[] latLng = CoordinateConverter.svy21ToWGS84(northDistance, eastDistance);

                CarParkEntity carPark = new CarParkEntity();
                carPark.setAddress(address);
                carPark.setLatitude(latLng[0]);
                carPark.setLongitude(latLng[1]);
                carPark.setTotalLots(totalLots);
                carParks.add(carPark);
            }

            return carParks;
        }
    }

    @Transactional
    public void loadCarParkData(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            List<CarParkEntity> carParks = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                String address = record.get("address");
                double northDistance = Double.parseDouble(record.get("y_coord"));
                double eastDistance = Double.parseDouble(record.get("x_coord"));
                int totalLots = Integer.parseInt(record.get("car_park_decks"));

                // Convert SVY21 to WGS84
                double[] latLng = CoordinateConverter.svy21ToWGS84(northDistance, eastDistance);

                // Create and add CarParkEntity
                CarParkEntity carPark = new CarParkEntity();
                carPark.setAddress(address);
                carPark.setLatitude(latLng[0]);
                carPark.setLongitude(latLng[1]);
                carPark.setTotalLots(totalLots);
                carParks.add(carPark);
            }

            // Save all car parks in batch
            carParkRepository.saveAll(carParks);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load car park data from CSV file", e);
        }
    }
}

