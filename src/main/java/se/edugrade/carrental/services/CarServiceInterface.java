package se.edugrade.carrental.services;

import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.vo.CarVO;

import java.util.List;

// Hugo Ransvi

public interface CarServiceInterface
{
    List<Car> getAvailableCars();
    List<CarVO> getAllCarsPublic();
    List<Car> getAllCars();
    Car addCar(Car car);
    Car updateCar(Car car);
    void removeCar(Long id);
}
