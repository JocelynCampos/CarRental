package se.edugrade.carrental.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.exceptions.ResourceNotFoundException;
import se.edugrade.carrental.vo.CarVO;
import se.edugrade.carrental.repositories.CarRepository;

import java.util.List;
import java.util.Optional;

// Hugo Ransvi

@Service
public class CarService implements CarServiceInterface
{
    @Autowired
    private CarRepository carRepository;

    @Override
    public List<Car> getAvailableCars() {
        return carRepository.findAll().stream()
                .filter(car -> car.getStatus() == Car.CarStatus.FREE)
                .toList();
    }

    @Override
    public List<Car> getAllCars() {
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
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Car updatedCar) {
        Optional<Car> existing = carRepository.findById(updatedCar.getId());
        if (existing.isPresent()) {
            return carRepository.save(updatedCar);
        }
        throw new ResourceNotFoundException("Car", "id", updatedCar.getId()); //Kastar RecourseNotFoundException istället för default exeption
    }

    @Override
    public void removeCar(Long id) {
        carRepository.deleteById(id);
    }
}
