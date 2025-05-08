package se.edugrade.carrental.services;

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
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class BookingService implements BookingServiceInterface {

    private BookingRepository bookingRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;
    private static final Logger adminLogger = Logger.getLogger("adminLogger");

    public BookingService(BookingRepository bookingRepository,CarRepository carRepository,UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }


    public Booking makeBooking(Long user_id, Long car_id, LocalDate startDate, LocalDate endDate){
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", user_id));
        Car car = carRepository.findById(car_id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", car_id));

        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setCar(car);
        newBooking.setDateWhenPickedUp(startDate);
        newBooking.setDateWhenTurnedIn(endDate);

        int totalCost = calculateTotalCost(startDate, endDate, car.getPricePerDay());
        newBooking.setTotalCost(totalCost);

        return bookingRepository.save(newBooking);

    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));
        if (!isBookingActiveOrFuture(booking)) {
            throw new IllegalStateException("Booking can not be cancelled.");
        }
        bookingRepository.delete(booking);
    }


    public void deleteBooking (Long booking_id, boolean isAdmin) {
        Booking booking = bookingRepository.findById(booking_id)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", booking_id));

        bookingRepository.deleteById(booking_id);
        if (isAdmin) {
            printDeletedBookingsToAdmin(booking);
        }
    }

    public Booking findBookingById(Long booking_id) {
        return getBookingById(booking_id);
    }

    public Booking getBookingById(Long booking_id) {
        return bookingRepository.findById(booking_id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", booking_id));
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

    public boolean isBookingActiveOrFuture(Booking booking) {
        LocalDate today = LocalDate.now();
        return booking.getDateWhenTurnedIn().isAfter(today);
    }

    public void deleteBookingsBeforeDate(LocalDate targetDate) {
        List<Booking> bookingsToDelete = bookingRepository.findAll().stream()
                .filter(booking -> booking.getDateWhenTurnedIn().isBefore(targetDate))
                .collect(Collectors.toList());
        bookingRepository.deleteAll(bookingsToDelete);

        adminLogger.info("Deleted " + bookingsToDelete.size() + " bookings before " + targetDate);
    }

    private void printDeletedBookingsToAdmin(Booking booking) {
        if (booking != null) {
            adminLogger.info("You deleted booking " + booking.getId()
            + "that belonged to user " + booking.getUser().getSocialSecurityNumber() +
                    " for car " + booking.getCar().getBrand() + booking.getCar() + booking.getCar().getModel());
        }
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


