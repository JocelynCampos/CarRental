package se.edugrade.carrental.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.exceptions.ResourceNotFoundException;
import se.edugrade.carrental.vo.CarVO;
import se.edugrade.carrental.repositories.CarRepository;


import java.util.List;

// Hugo Ransvi

@Service
public class CarService implements CarServiceInterface
{
    final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    private static final Logger adminLogger = LoggerFactory.getLogger("AdminLogger");

    @Override
    public List<Car> getAvailableCars() {
        adminLogger.info("ADMIN: Getting all available cars");
        return carRepository.findAll().stream()
                .filter(car -> car.getStatus() == Car.CarStatus.FREE)
                .toList();
    }

    @Override
    public List<Car> getAllCars() {
        adminLogger.info("ADMIN: Getting all cars");
        return carRepository.findAll();
    }

    @Override
    public List<CarVO> getAllCarsPublic() {
        adminLogger.info("CUSTOMER: Getting all public available cars");
        return carRepository.findAll().stream()
                .filter(car -> car.getStatus() == Car.CarStatus.FREE)
                .map(car -> new CarVO(
                        car.getId(),
                        car.getPricePerDay(),
                        car.getBrand(),
                        car.getModel(),
                        car.getStatus()
                ))
                .toList();
    }

    @Override
    public Car addCar(Car car) {
        adminLogger.info("ADMIN: adding car: {}", car);
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Car updatedCar) {
        return carRepository.findById(updatedCar.getId())
                .map(existing -> {
                    adminLogger.info("ADMIN: Updating car with id: {}", updatedCar);
                    return carRepository.save(updatedCar);
                })
                .orElseThrow(() -> {
                    adminLogger.warn("ADMIN: Car with ID: {} could not be updated - not found", updatedCar.getId());
                    return new ResourceNotFoundException("Car", "id", updatedCar.getId());
                });
    }

    @Override
    public void removeCar(Long id) {
        if (!carRepository.existsById(id)) {
            adminLogger.warn("ADMIN: attempt to remove car with ID {} - not found", id);
            throw new ResourceNotFoundException("Car", "id", id);
        }
        adminLogger.info("ADMIN: removing car with ID {}", id);
        carRepository.deleteById(id);
    }
}
