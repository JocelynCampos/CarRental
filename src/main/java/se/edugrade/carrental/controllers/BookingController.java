package se.edugrade.carrental.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.services.BookingService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /************************ CUSTOMER ENDPOINTS ******************************/

    @PostMapping("/addorder")
    public Booking createOrder(@RequestParam Long userId,
                               @RequestParam Long carId,
                               @RequestParam String startDate,
                               @RequestParam String endDate) {

        return bookingService.makeBooking(
                userId,
                carId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
    }

    @GetMapping("/activeorders")
    public List<Booking> getActiveOrders() {
        return bookingService.getActiveOrders();
    }

    @GetMapping("/orders")
    public List<Booking> getExpiredOrders() {
        return bookingService.expiredBookings();
    }

    /************************* ADMIN ENDPOINTS ***************************/

    @GetMapping("/admin/activeorders")
    public List<Booking> getAllActiveOrders() {
        return bookingService.getActiveOrders();
    }

    @GetMapping("/admin/orders")
    public List<Booking> getAllExpiredOrders() {
        return bookingService.expiredBookings();
    }


    @DeleteMapping("/admin/removeorder")
    public ResponseEntity <Void> deleteOrder(@RequestParam Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/removeorders-beforedate/{date}")
    public ResponseEntity<Void> deleteOrdersBeforeDate(@PathVariable String date) {
        LocalDate targetDate = LocalDate.parse(date);
        bookingService.deleteBookingsBeforeDate(targetDate);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/statistics")
    public String getStatistics() {
        return "unfinished method";

    }



}


