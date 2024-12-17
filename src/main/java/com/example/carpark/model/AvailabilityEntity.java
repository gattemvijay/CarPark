package com.example.carpark.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "availability")
public class AvailabilityEntity {

    @Id
    private Long carParkId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "car_park_id")
    private CarParkEntity carPark;
    private String address; // New field for address
    private Double latitude;
    private Double longitude;
    private Integer totalLots;

    private int availableLots;
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_park_id", referencedColumnName = "id")
    private CarParkEntity carParkEntity;

    public AvailabilityEntity() {

    }

    public Long getCarParkId() {
        return carParkId;
    }

    public void setCarParkId(Long carParkId) {
        this.carParkId = carParkId;
    }

    public int getAvailableLots() {
        return availableLots;
    }

    public void setAvailableLots(int availableLots) {
        this.availableLots = availableLots;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CarParkEntity getCarPark() {
        return carPark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getTotalLots() {
        return totalLots;
    }

    public void setTotalLots(Integer totalLots) {
        this.totalLots = totalLots;
    }

    public void setCarPark(CarParkEntity carPark) {
        this.carPark = carPark;
    }

    public CarParkEntity getCarParkEntity() {
        return carParkEntity;
    }

    public void setCarParkEntity(CarParkEntity carParkEntity) {
        this.carParkEntity = carParkEntity;
    }

    public AvailabilityEntity(Long carParkId, CarParkEntity carPark, int availableLots, LocalDateTime updatedAt) {
        this.carParkId = carParkId;
        this.carPark = carPark;
        this.availableLots = availableLots;
        this.updatedAt = updatedAt;
    }

}
