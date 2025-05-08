package se.edugrade.carrental.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.services.BookingService;

import java.security.Principal;
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
    public Booking addOrder(@RequestParam Long userId,
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
    public ResponseEntity<String> cancelOrder(@RequestParam Long bookingId, Principal principal) {
        Booking booking = bookingService.getBookingById(bookingId);

        String loggedInUser = principal.getName();

        //Ser till att avbokingen sker av respektive användare
        if (!booking.getUser().getSocialSecurityNumber().equals(loggedInUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not your booking to cancel.");
        }

        if (!bookingService.isBookingActiveOrFuture(booking)) {
            return ResponseEntity.badRequest().body("Cancellation of previous bookings not possible.");
        }

        bookingService.cancelBooking(bookingId);

        return ResponseEntity.ok("Booking cancelled.");
    }

    @GetMapping("/activeorders") //Se aktiva bokningar
    public ResponseEntity<List<Booking>> activeOrders() {
        List<Booking> activeOrders = bookingService.getActiveOrders();
        if (activeOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activeOrders);
    }

    @GetMapping("/orders") //se tidigare bokningar
    public ResponseEntity<List<Booking>> orders() {
        List<Booking> orders = bookingService.expiredBookings();
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    /************************* ADMIN ENDPOINTS ***************************/

    @GetMapping("/admin/activeorders") //Lista alla aktiva ordrar
    public ResponseEntity<List<Booking>> adminActiveOrders() {
        List<Booking> adminActiveOrders = bookingService.getActiveOrders();
        if (adminActiveOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(adminActiveOrders);
    }

    @GetMapping("/admin/orders") // Lista historiska ordrar
    public ResponseEntity <List<Booking>> adminOrders() {
        List<Booking> adminExpiredOrders = bookingService.expiredBookings();
        if (adminExpiredOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(adminExpiredOrders);
    }


    @DeleteMapping("/admin/removeorder") //Ta bort bokning från systemet
    public ResponseEntity <Void> adminRemoveOrder(@RequestParam Long bookingId) {
        bookingService.deleteBooking(bookingId, true);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/removeorders-beforedate/{date}") //
    public ResponseEntity<Void> adminRemoveOrdersBeforeDate(@PathVariable String date) {
        LocalDate targetDate = LocalDate.parse(date);
        bookingService.deleteBookingsBeforeDate(targetDate);
        return ResponseEntity.noContent().build();
    }

}


