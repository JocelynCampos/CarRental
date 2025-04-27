package se.edugrade.carrental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.repositories.BookingRepository;

import java.util.List;

@Controller
public class BookingController {

    private final BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository1) {
        this.bookingRepository = bookingRepository1;
    }


  /*  • GET /api/v1/admin/activeorders - Lista alla aktiva ordrar
• GET /api/v1/admin/orders - Lista historiska ordrar
• DELETE /api/v1/admin/removeorder - Ta bort bokning från systemet
• DELETE /api/v1/admin/removeorders-beforedate/{date}
• GET /api/v1/admin/statistics - Visa statistik såsom mest hyrda bilmärke under en viss
    period. Antal gånger varje bil hyrts ut, vanligaste hyresperiod (antal dagar), genomsnittlig
    kostnad per hyresorder. Total intäkt per bil. Total intäkt under en viss tidsperiod. (Till
    denna endpoint kan man ange pathvariables för att kunna visa specifik information) (Utrymme
    för kreativitet)
    */

}
