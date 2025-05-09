package se.edugrade.carrental.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.services.*;

import java.util.List;
                      // Kamran Akbari
@RestController
@RequestMapping("/api/v1")
public class CustomerController{

    private final BookingService bookingService;
    private final CarService carService;
    private final UserService userService;

    public CustomerController(BookingService bookingService, CarService carService, UserService userService) {
        this.bookingService = bookingService;
        this.carService = carService;
        this.userService = userService;
    }

    @GetMapping ("/cars")
public ResponseEntity<List<Car>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
}

@PostMapping ("/addorder")
public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
    return ResponseEntity.ok(bookingService.createBooking(booking));
}

@PutMapping("/cancelorder/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
    bookingService.cancel(bookingId);
    return ResponseEntity.ok("Booking with ID " + bookingId + " cancelled successfully. ");
}

@GetMapping ("/activeorders/{personnummer}")
    public ResponseEntity<List<Booking>> getActiveBookings(@PathVariable String personnummer) {
    return ResponseEntity.ok(bookingService.getActiveBookingsForCustomer(personnummer));
}

@GetMapping ("/order/{personnummer}")
    public ResponseEntity<List<Booking>> getPastBookings(@PathVariable String personnummer) {
    return ResponseEntity.ok(bookingService.getPastBookingForCustomer(personnummer));
}

@PutMapping ("/updateinfo/{userId}")
    public ResponseEntity<User> updateUserInfo(@PathVariable Long userId, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUserInfo(userId,updatedUser));
}

}


