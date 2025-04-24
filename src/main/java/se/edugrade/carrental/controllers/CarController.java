package se.edugrade.carrental.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.edugrade.carrental.entities.Car;
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
    public List<Car> getAvailableCars() {
        return carService.getAvailableCars();
    }

    // Admin Endpoints

    @GetMapping("/admin/cars")
    public List<Car> getAvailableCarsAdmin() {
        return carService.getAvailableCars();
    }

    @GetMapping("/admin/allcars")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping("/admin/addcar")
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @PutMapping("/admin/updatecar")
    public Car updateCar(@RequestBody Car car) {
        return carService.updateCar(car);
    }

    @DeleteMapping("/admin/removecar")
    public void deleteCar(@RequestParam Long id) {
        carService.removeCar(id);
    }
}
