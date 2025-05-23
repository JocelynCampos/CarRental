package se.edugrade.carrental.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.exceptions.BookingCancellationException;
import se.edugrade.carrental.exceptions.ResourceNotFoundException;
import se.edugrade.carrental.helpMethods.BookingCalculator;
import se.edugrade.carrental.repositories.BookingRepository;
import se.edugrade.carrental.repositories.CarRepository;
import se.edugrade.carrental.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class BookingService implements BookingServiceInterface {

    private BookingRepository bookingRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;

    private static final Logger adminLogger = LoggerFactory.getLogger("AdminLogger");

    public BookingService(BookingRepository bookingRepository,CarRepository carRepository,UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    /*********************** Användare ***************************/

    //Skapa order
    public Booking createBooking(Long user_id, Long car_id, LocalDate startDate, LocalDate endDate){
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", user_id));
        Car car = carRepository.findById(car_id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", car_id));

        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setCar(car);
        newBooking.setDateWhenPickedUp(startDate);
        newBooking.setDateWhenTurnedIn(endDate);

        int totalCost = BookingCalculator.calculateTotalCost(startDate, endDate, car.getPricePerDay());
        newBooking.setTotalCost(totalCost);

        return bookingRepository.save(newBooking);

    }

    //Avboka order
    public void cancelUserBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));
        if (!isBookingActiveOrFuture(booking)) {
            throw new BookingCancellationException("Booking: " + bookingId + "can not be cancelled.");
        }
        bookingRepository.delete(booking);
    }

    //Se aktiva bokningar
    public List<Booking> getUserActiveOrders(Long userId) {
        LocalDate today = LocalDate.now();
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getUser().getId().equals(userId))
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.ACTIVE)
                .filter(booking -> booking.getDateWhenPickedUp().isBefore(today) &&
                        booking.getDateWhenTurnedIn().isAfter(today))
                .collect(Collectors.toList());
    }

    //Se tidigare bokningar
    public List<Booking> getUserExpiredBookings(Long userId) {
        LocalDate today = LocalDate.now();
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getUser().getId().equals(userId))
                .filter(booking -> booking.getDateWhenTurnedIn().isBefore(today))
                .collect(Collectors.toList());
    }

    /************************ Admin **************************/

    //Aktiva ordrar
    public List<Booking> getAdminActiveOrders() {
        LocalDate today = LocalDate.now();
        adminLogger.info("ADMIN: get active orders.");
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.ACTIVE)
                .filter(booking -> booking.getDateWhenPickedUp().isBefore(today)
                        && booking.getDateWhenTurnedIn().isAfter(today))
                .collect(Collectors.toList());
    }

    //Lista historiska ordrar
    public List<Booking> getAdminExpiredBookings() {
        LocalDate today = LocalDate.now();
        adminLogger.info("ADMIN: get expired orders.");
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getDateWhenTurnedIn().isBefore(today))
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    //Ta bort bokning från systemet
    public void deleteBookingAdmin(Long booking_id, boolean isAdmin) {
        Booking booking = bookingRepository.findById(booking_id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", booking_id));

        bookingRepository.deleteById(booking_id);
        if (isAdmin) {
            printDeletedBookingsToAdmin(booking);
        }
    }

    //Ta bort bokning före specifikt datum
    public void deleteBookingsBeforeDate(LocalDate targetDate) {
        List<Booking> bookingsToDelete = bookingRepository.findAll().stream()
                .filter(booking -> booking.getDateWhenTurnedIn().isBefore(targetDate))
                .collect(Collectors.toList());
        bookingRepository.deleteAll(bookingsToDelete);

        adminLogger.info("Deleted " + bookingsToDelete.size() + " bookings before " + targetDate);
    }


    public Booking findBookingById(Long booking_id) {
        return getBookingById(booking_id);
    }

    public Booking getBookingById(Long booking_id) {
        adminLogger.info("Get booking by id: {}", booking_id);
        return bookingRepository.findById(booking_id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", booking_id));
    }

    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public boolean isBookingActiveOrFuture(Booking booking) {
        LocalDate today = LocalDate.now();
        adminLogger.info("Checking if booking is active or future: {}", booking);
        return booking.getDateWhenTurnedIn().isAfter(today);
    }


    private void printDeletedBookingsToAdmin(Booking booking) {
        if (booking != null) {
            adminLogger.info("You deleted booking {} for user {}. Car: {} {}",
            booking.getId(),
            booking.getUser().getSocialSecurityNumber(),
            booking.getCar().getBrand(),
            booking.getCar().getModel());
        }
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


