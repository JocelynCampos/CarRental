package se.edugrade.carrental.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<CarVO>> getAllPublicCars() {
        List<CarVO> cars = carService.getAllCarsPublic();
        return ResponseEntity.ok(cars);
    }

    // Admin Endpoints

    @GetMapping("/admin/cars")
    public ResponseEntity<List<Car>> getAvailableCars() {
        List<Car> cars = carService.getAvailableCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/admin/allcars")
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @PostMapping("/admin/addcar")
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        Car saved = carService.addCar(car);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/admin/updatecar")
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        Car updated = carService.updateCar(car);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/admin/removecar")
    public ResponseEntity<Void> deleteCar(@RequestParam Long id) {
        carService.removeCar(id);
        return ResponseEntity.noContent().build();
    }
}
