package se.edugrade.carrental.entities;

import jakarta.persistence.*;

@Entity
@Table (name = "cars")
public class Car
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_per_day", nullable = false)
    private Integer pricePerDay;

    @Column(name = "brand", nullable = false, length = 40)
    private String brand;

    @Column(name = "model", nullable = false, length = 40)
    private String model;

    @Column(name = "registration_number", nullable = false, length = 10)
    private String registrationNumber;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarStatus status;

    public Car() {
    }

    public Car(Integer pricePerDay, String brand, String model, String registrationNumber, CarStatus status) {
        this.pricePerDay = pricePerDay;
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Integer pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public enum CarStatus
    {
        BOOKED,
        SERVICE,
        FREE
    };
}
