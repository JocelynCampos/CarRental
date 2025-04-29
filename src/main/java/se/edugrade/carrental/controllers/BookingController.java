package se.edugrade.carrental.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.repositories.BookingRepository;
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

    /******************************************************************************************************
    • POST /api/v1/addorder - Skapa order (hyra bil)
    • GET /api/v1/activeorders - Se aktiva bokningar
    • GET /api/v1/orders - Se tidigare bokningar
    • GET /api/v1/admin/activeorders - Lista alla aktiva ordrar
    • GET /api/v1/admin/orders - Lista historiska ordrar
    • DELETE /api/v1/admin/removeorder - Ta bort bokning från systemet
    • DELETE /api/v1/admin/removeorders-beforedate/{date}
    • GET /api/v1/admin/statistics - Visa statistik såsom mest hyrda bilmärke under en viss
        period. Antal gånger varje bil hyrts ut, vanligaste hyresperiod (antal dagar), genomsnittlig
        kostnad per hyresorder. Total intäkt per bil. Total intäkt under en viss tidsperiod. (Till
        denna endpoint kan man ange pathvariables för att kunna visa specifik information) (Utrymme
        för kreativitet)
     *********************************************************************************************************/
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

    }

    @GetMapping("/orders")
    public List<Booking> getOrders() {

    }

    @DeleteMapping("/admin/removeorder") {

    }

    @DeleteMapping("/admin/removeorders-beforedate/{id}") {

    }

    @GetMapping("/admin/statistics") {

    }



}


