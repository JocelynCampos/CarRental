package se.edugrade.carrental.services;

import se.edugrade.carrental.entities.Car;

import java.util.List;

// Hugo Ransvi

public interface CarServiceInterface
{
    List<Car> getAvailableCars();
    List<Car> getAllCars();
    Car addCar(Car car);
    Car updateCar(Car car);
    void removeCar(Long id);
}
