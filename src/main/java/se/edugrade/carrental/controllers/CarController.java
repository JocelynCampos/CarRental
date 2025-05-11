package se.edugrade.carrental.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.vo.CarVO;
import se.edugrade.carrental.services.CarServiceInterface;

import java.util.List;

// Hugo Ransvi

@RestController
@RequestMapping("/api/v1")
public class CarController
{
    @Autowired
    private CarServiceInterface carService;

    // Kund Endpoints

    @GetMapping("/cars")
    public List<CarVO> getAllPublicCars() {
        return carService.getAllCarsPublic();
    }

    // Admin Endpoints

    @GetMapping("/admin/cars")
    public List<Car> getAvailableCars() {
        return carService.getAvailableCars();
    }

    @GetMapping("/admin/allcars")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping("/admin/addcar")
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        Car saved = carService.addCar(car);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/admin/updatecar")
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.updateCar(car));
    }

    @DeleteMapping("/admin/removecar")
    public ResponseEntity<Void> deleteCar(@RequestParam Long id) {
        carService.removeCar(id);
        return ResponseEntity.noContent().build();
    }
}
