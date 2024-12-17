package com.example.carpark.model;

import jakarta.persistence.*;

@Entity
@Table(name = "car_parks")
public class CarParkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "car_park_number", unique = true, nullable = false)
    private String carParkNumber;

    private String address;
    private double latitude;

    private double longitude;

    @Column(name = "total_lots", nullable = false)
    private int totalLots;

    @OneToOne(mappedBy = "carPark", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AvailabilityEntity availability;

    public CarParkEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public CarParkEntity(Long id, String address, double latitude, double longitude, int totalLots, AvailabilityEntity availability) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalLots = totalLots;
        this.availability = availability;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getTotalLots() {
        return totalLots;
    }

    public void setTotalLots(int totalLots) {
        this.totalLots = totalLots;
    }

    public Integer getAvailableLots() {
        return availability != null ? availability.getAvailableLots() : null;
    }

    public String getCarParkNumber() {
        return carParkNumber;
    }

    public void setCarParkNumber(String carParkNumber) {
        this.carParkNumber = carParkNumber;
    }

    public AvailabilityEntity getAvailability() {
        return availability;
    }

    public void setAvailability(AvailabilityEntity availability) {
        this.availability = availability;
    }

}
