package se.edugrade.carrental.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.services.BookingService;
import se.edugrade.carrental.services.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @Autowired
    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    private User getLoggedInUser(Principal principal) {
        return userService.findBySocialSecurityNumber(principal.getName());
    }

    /************************ CUSTOMER ENDPOINTS ******************************/

    @PostMapping("/addorder") //Skapa order
    public ResponseEntity<Booking> addOrder(@RequestParam Long userId,
                            @RequestParam Long carId,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {


        Booking booking = bookingService.createBooking(userId, carId, startDate, endDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
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

        bookingService.cancelUserBooking(bookingId);

        return ResponseEntity.ok("Booking cancelled.");
    }

    @GetMapping("/activeorders") //Se aktiva bokningar användare
    public ResponseEntity<List<Booking>> activeOrders(Principal principal) {

        User user = getLoggedInUser(principal);
        List<Booking> activeOrders = bookingService.getUserActiveOrders(user.getId());

        if (activeOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activeOrders);
    }

    @GetMapping("/orders") //se tidigare bokningar
    public ResponseEntity<List<Booking>> orders (Principal principal) {
        User user = getLoggedInUser(principal);
        List<Booking> orders = bookingService.getUserExpiredBookings(user.getId());

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    /************************* ADMIN ENDPOINTS ***************************/

    @GetMapping("/admin/activeorders") //Lista alla aktiva ordrar
    public ResponseEntity<List<Booking>> adminActiveOrders() {
        List<Booking> adminActiveOrders = bookingService.getAdminActiveOrders();
        if (adminActiveOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(adminActiveOrders);
    }

    @GetMapping("/admin/orders") // Lista historiska ordrar
    public ResponseEntity <List<Booking>> adminOrders() {
        List<Booking> adminExpiredOrders = bookingService.getAdminExpiredBookings();
        if (adminExpiredOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(adminExpiredOrders);
    }


    @DeleteMapping("/admin/removeorder") //Ta bort bokning från systemet
    public ResponseEntity <Void> adminRemoveOrder(@RequestParam Long bookingId) {
        bookingService.deleteBookingAdmin(bookingId, true);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/removeorders-beforedate/{date}") //
    public ResponseEntity<Void> adminRemoveOrdersBeforeDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        bookingService.deleteBookingsBeforeDate(date);
        return ResponseEntity.noContent().build();
    }



}