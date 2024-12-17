package com.example.carpark.task;


import com.example.carpark.model.AvailabilityEntity;
import com.example.carpark.model.CarParkEntity;
import com.example.carpark.repository.AvailabilityRepository;
import com.example.carpark.repository.CarParkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@Service
public class CarParkAvailabilityUpdater {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private CarParkRepository carParkRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CARPARK_AVAILABILITY_URL =
            "https://api.data.gov.sg/v1/transport/carpark-availability";

    @Scheduled(fixedRate = 3600000) // Run every 1 hour (3600000 ms)
    public void updateAvailability() {
        try {
            // Fetch data from the external API
            String response = restTemplate.getForObject(CARPARK_AVAILABILITY_URL, String.class);

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode carparkData = rootNode.path("items").get(0).path("carpark_data");

            // Iterate over carpark data
            for (JsonNode carparkNode : carparkData) {
                JsonNode addressNode = carparkNode.get("address");
                String address = addressNode != null ? addressNode.asText() : "Unknown Address";
                String carParkNumber = carparkNode.path("carpark_number").asText();
                JsonNode availabilityInfo = carparkNode.path("carpark_info").get(0);

                int totalLots = availabilityInfo.path("total_lots").asInt();
                int availableLots = availabilityInfo.path("lots_available").asInt();

                // Check if CarParkEntity exists
                CarParkEntity carParkEntity = carParkRepository
                        .findByCarParkNumber(carParkNumber)
                        .orElse(null);

                if (carParkEntity != null) {
                    // Create or update AvailabilityEntity
                    AvailabilityEntity availability = availabilityRepository
                            .findByCarParkId(carParkEntity.getId())
                            .orElse(new AvailabilityEntity());
                    availability.setAddress(address);
                    availability.setCarParkEntity(carParkEntity); // Link to the existing car park entity
                    availability.setAvailableLots(availableLots);

                    // Save the updated entity
                    availabilityRepository.save(availability);
                }
            }
        } catch (IOException e) {
            // Log or handle the exception
            e.printStackTrace();
        }
    }

    private void parseAndSave(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode carParks = rootNode.path("items").get(0).path("carpark_data");
            Iterator<JsonNode> iterator = carParks.elements();

            while (iterator.hasNext()) {
                JsonNode carParkNode = iterator.next();

                // Extract fields from JSON
                String carParkNumber = carParkNode.path("carpark_number").asText();
                JsonNode availabilityNode = carParkNode.path("carpark_info").get(0);
                int availableLots = availabilityNode.path("lots_available").asInt();

                // Find the CarParkEntity associated with this carParkNumber
                Optional<CarParkEntity> carParkEntityOptional = carParkRepository.findByCarParkNumber(carParkNumber);
                if (carParkEntityOptional.isPresent()) {
                    CarParkEntity carParkEntity = carParkEntityOptional.get();

                    // Update availability for the car park
                    AvailabilityEntity availability = new AvailabilityEntity();
                    availability.setCarParkEntity(carParkEntity); // Set the relationship
                    availability.setAvailableLots(availableLots); // Set available lots

                    availabilityRepository.save(availability);
                } else {
                    System.out.println("Car park not found for carParkNumber: " + carParkNumber);
                }
            }

            System.out.println("Car park availability updated successfully.");
        } catch (Exception ex) {
            System.err.println("Error occurred while parsing and saving car park data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
