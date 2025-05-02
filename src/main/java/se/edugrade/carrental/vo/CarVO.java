package se.edugrade.carrental.vo;

import se.edugrade.carrental.entities.Car.CarStatus;

public class CarVO {
    private Long id;
    private Integer pricePerDay;
    private String brand;
    private String model;
    private CarStatus status;

    public CarVO(Long id, Integer pricePerDay, String brand, String model, CarStatus status) {
        this.id = id;
        this.pricePerDay = pricePerDay;
        this.brand = brand;
        this.model = model;
        this.status = status;
    }

    // Getters
    public Long getId() { return id; }
    public Integer getPricePerDay() { return pricePerDay; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public CarStatus getStatus() { return status; }
}
