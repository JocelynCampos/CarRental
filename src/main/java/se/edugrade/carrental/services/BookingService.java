package se.edugrade.carrental.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.exceptions.ResourceNotFoundException;
import se.edugrade.carrental.repositories.BookingRepository;
import se.edugrade.carrental.repositories.CarRepository;
import se.edugrade.carrental.repositories.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService implements BookingServiceInterface {


    private static final Logger adminLogger = LoggerFactory.getLogger("AdminLogger");

    private BookingRepository bookingRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,CarRepository carRepository,UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }


    public Booking makeBooking(Long user_id, Long car_id, LocalDate startDate, LocalDate endDate){
        adminLogger.info("Retrieving user with ID: {}", user_id);
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", user_id));
        adminLogger.info("Retrieving car with ID: {}", car_id);
        Car car = carRepository.findById(car_id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", car_id));

        adminLogger.info("Saving new Booking to database.");
        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setCar(car);
        newBooking.setDateWhenPickedUp(startDate);
        newBooking.setDateWhenTurnedIn(endDate);

        int totalCost = calculateTotalCost(startDate, endDate, car.getPricePerDay());
        newBooking.setTotalCost(totalCost);

        adminLogger.info("Save successful.");
        return bookingRepository.save(newBooking);

    }

    public Booking saveBooking(Booking booking ) {
        adminLogger.info("Saving new Booking to database.");
        return bookingRepository.save(booking);
    }

    public void deleteBooking (Long booking_id) {
        adminLogger.info("Deleting booking with ID: {}", booking_id);
        bookingRepository.deleteById(booking_id);
    }

    public Booking update(Booking booking) {
        adminLogger.info("Updating booking with ID: {}", booking.getId());
        return bookingRepository.save(booking);
    }

    public Booking findBookingById(Long booking_id) {
        adminLogger.info("Retrieving booking with ID: {}", booking_id);
        return getBookingById(booking_id);
    }

    public Booking getBookingById(Long booking_id) {
        return bookingRepository.findById(booking_id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", booking_id));
    }

    public List<Booking> findAllBookings() {
        adminLogger.info("Retrieving all bookings from database.");
        return bookingRepository.findAll();
    }

    public List<Booking> getActiveOrders() {
        LocalDate today = LocalDate.now();
        adminLogger.info("Retrieving active bookings from database.");
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getDateWhenPickedUp().isBefore(today) && booking.getDateWhenTurnedIn().isAfter(today))
                .collect(Collectors.toList());
    }

    public List<Booking> expiredBookings() {
        LocalDate today = LocalDate.now();
        adminLogger.info("Retrieving expired bookings from database.");
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getDateWhenTurnedIn().isBefore(today))
                .collect(Collectors.toList());
    }

    public void deleteBookingsBeforeDate(LocalDate targetDate) {
        adminLogger.info("Deleting bookings from database set to expire before {}.", targetDate);
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


