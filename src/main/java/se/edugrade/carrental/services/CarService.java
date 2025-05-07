package se.edugrade.carrental.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.vo.CarVO;
import se.edugrade.carrental.repositories.CarRepository;

import java.util.List;
import java.util.Optional;

// Hugo Ransvi

@Service
public class CarService implements CarServiceInterface
{
    private final static Logger adminLogger = LoggerFactory.getLogger("adminLogger");

    @Autowired
    private CarRepository carRepository;

    @Override
    public List<Car> getAvailableCars() {
        adminLogger.info("Retrieving all free cars from database.");
        return carRepository.findAll().stream()
                .filter(car -> car.getStatus() == Car.CarStatus.FREE)
                .toList();
    }

    @Override
    public List<Car> getAllCars() {
        adminLogger.info("Retrieving all cars from database.");
        return carRepository.findAll();
    }

    @Override
    public List<CarVO> getAllCarsPublic() {
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
        adminLogger.info("Saving car with ID: {}", car.getId());
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Car updatedCar) {
        adminLogger.info("Retrieving car with ID: {}", updatedCar.getId());
        Optional<Car> existing = carRepository.findById(updatedCar.getId());
        if (existing.isPresent()) {
            adminLogger.info("Updating car with ID: {}", updatedCar.getId());
            return carRepository.save(updatedCar);
        }
        adminLogger.warn("Retrieving car failed.");
        throw new RuntimeException("Car not found");
    }

    @Override
    public void removeCar(Long id) {
        adminLogger.info("Removing car with ID: {}", id);
        carRepository.deleteById(id);
    }
}
