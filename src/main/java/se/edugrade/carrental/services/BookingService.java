package se.edugrade.carrental.services;

import org.springframework.stereotype.Service;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.repositories.BookingRepository;
import se.edugrade.carrental.repositories.CarRepository;
import se.edugrade.carrental.repositories.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService implements BookingServiceInterface {

    private BookingRepository bookingRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,CarRepository carRepository,UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }


    public Booking makeBooking(Long user_id, Long car_id, LocalDate startDate, LocalDate endDate){
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        Car car = carRepository.findById(car_id)
                .orElseThrow(() -> new RuntimeException("Car Not Found"));

        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setCar(car);
        newBooking.setDateWhenPickedUp(startDate);
        newBooking.setDateWhenTurnedIn(endDate);

        int totalCost = calculateTotalCost(startDate, endDate, car.getPricePerDay());
        newBooking.setTotalCost(totalCost);

        return bookingRepository.save(newBooking);

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
 **********************************************************************************************/

    public Booking saveBooking(Booking booking ) {
        return bookingRepository.save(booking);
    }

    public void deleteBooking (Long booking_id) {
        bookingRepository.deleteById(booking_id);
    }

    public Booking update(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking findBookingById(Long booking_id) {
        return getBookingById(booking_id);
    }

    public Booking getBookingById(Long booking_id) {
        return bookingRepository.findById(booking_id)
                .orElseThrow(() -> new RuntimeException("Booking Not Found"));
    }

    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getActiveOrders() {
        LocalDate today = LocalDate.now();
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getDateWhenPickedUp().isBefore(today) && booking.getDateWhenTurnedIn().isAfter(today))
                .collect(Collectors.toList());
    }

    public List<Booking> expiredBookings() {
        LocalDate today = LocalDate.now();
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getDateWhenTurnedIn().isBefore(today))
                .collect(Collectors.toList());
    }

    public void deleteBookingsBeforeDate(LocalDate targetDate) {
        List<Booking> bookingsToDelete = bookingRepository.findAll().stream()
                .filter(booking -> booking.getDateWhenTurnedIn().isBefore(targetDate))
                .collect(Collectors.toList());
        bookingRepository.deleteAll(bookingsToDelete);
    }


    @Override
    public int calculateTotalCost(LocalDate startDate, LocalDate endDate, int pricePerDay) {
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        return (int) (numberOfDays * pricePerDay);
    }

    public BookingRepository getBookingRepository() {
        return bookingRepository;
    }

    public CarRepository getCarRepository() {
        return carRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

}


