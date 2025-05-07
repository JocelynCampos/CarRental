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

    @PostMapping("/addorder") //Skapa order
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

    @PutMapping("/cancelorder") //Avboka order
    public ResponseEntity<String> cancelOrder(@RequestParam Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking cancelled");
    }

    @GetMapping("/activeorders") //Se aktiva bokningar
    public ResponseEntity<List<Booking>> getActiveOrders() {
        List<Booking> activeOrders = bookingService.getActiveOrders();
        if (activeOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activeOrders);
    }

    @GetMapping("/orders") //se tidigare bokningar
    public ResponseEntity<List<Booking>> getExpiredOrders() {
        List<Booking> orders = bookingService.expiredBookings();
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    /************************* ADMIN ENDPOINTS ***************************/

    @GetMapping("/admin/activeorders") //Lista alla aktiva ordrar
    public ResponseEntity<List<Booking>> getAllActiveOrders() {
        List<Booking> adminActiveOrders = bookingService.getActiveOrders();
        if (adminActiveOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookingService.getActiveOrders());
    }

    @GetMapping("/admin/orders") // Lista historiska ordrar
    public ResponseEntity <List<Booking>> getAllExpiredOrders() {
        List<Booking> adminExpiredOrders = bookingService.expiredBookings();
        if (adminExpiredOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(adminExpiredOrders);
    }


    @DeleteMapping("/admin/removeorder") //Ta bort bokning från systemet
    public ResponseEntity <Void> deleteOrder(@RequestParam Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/removeorders-beforedate/{date}") //
    public ResponseEntity<Void> deleteOrdersBeforeDate(@PathVariable String date) {
        LocalDate targetDate = LocalDate.parse(date);
        bookingService.deleteBookingsBeforeDate(targetDate);
        return ResponseEntity.noContent().build();
    }

}


