package se.edugrade.carrental.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.repositories.CarRepository;
import se.edugrade.carrental.vo.CarVO;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Hugo Ransvi

public class CarServiceTest {

    private CarRepository carRepository;
    private CarService carService;

    @BeforeEach
    void setUp() {
        carRepository = mock(CarRepository.class);
        carService = new CarService(carRepository);
    }

    @Test
    void testGetAvailableCars() {
        Car car1 = new Car(400, "Toyota", "Corolla", "ABC123", Car.CarStatus.FREE);
        Car car2 = new Car(200, "Mitsubishi", "Space Star", "DEF456", Car.CarStatus.BOOKED);

        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));
        List<Car> result = carService.getAvailableCars();

        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getBrand());
    }

    @Test
    void testAddCar() {
        Car car = new Car(600, "BMW", "M3", "GHI789", Car.CarStatus.FREE);
        when(carRepository.save(car)).thenReturn(car);

        Car saved = carService.addCar(car);
        assertEquals(car, saved);
    }

    @Test
    void testGetAllCarsPublic() {
        Car car1 = new Car(300, "Volkswagen", "Golf", "ABC123", Car.CarStatus.FREE);
        Car car2 = new Car( 400, "Ford", "Focus", "DEF456", Car.CarStatus.BOOKED);

        when(carRepository.findAll()).thenReturn(List.of(car1, car2));

        List<CarVO> result = carService.getAllCarsPublic();

        assertEquals(1, result.size());
        assertEquals("Volkswagen", result.get(0).getBrand());
    }
}
