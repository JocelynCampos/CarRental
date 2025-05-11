package se.edugrade.carrental.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.repositories.CarRepository;
import se.edugrade.carrental.vo.CarVO;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Hugo Ransvi

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarService carService;

    @Test
    void getAvailableCars_returnsOnlyFreeCars() {
        Car freeCar = new Car(400, "Volvo", "V70", "ABC123", Car.CarStatus.FREE);
        Car bookedCar = new Car(800, "Toyota", "Supra MK3", "DEF456", Car.CarStatus.BOOKED);
        when(carRepository.findAll()).thenReturn(Arrays.asList(freeCar, bookedCar));

        List<Car> result = carService.getAvailableCars();

        assertEquals(1, result.size());
        assertEquals(Car.CarStatus.FREE, result.get(0).getStatus());
    }

    @Test
    void getAllCarsPublic_onlyFreeCarsReturnedAsVO() {
        Car freeCar = new Car(700, "Volvo", "XC60", "ABC123", Car.CarStatus.FREE);
        Car bookedCar = new Car(650, "Toyota", "Camry", "DEF456", Car.CarStatus.BOOKED);
        when(carRepository.findAll()).thenReturn(Arrays.asList(freeCar, bookedCar));

        List<CarVO> result = carService.getAllCarsPublic();

        assertEquals(1, result.size());
        assertEquals("Volvo", result.get(0).getBrand());
    }

    @Test
    void addCar_saveCar() {
        Car car = new Car(500, "Audi", "Q5", "ABC123", Car.CarStatus.FREE);
        when(carRepository.save(car)).thenReturn(car);

        Car saved = carService.addCar(car);

        assertEquals("Audi", saved.getBrand());
        verify(carRepository).save(car);
    }
}
