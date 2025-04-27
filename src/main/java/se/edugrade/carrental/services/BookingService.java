package se.edugrade.carrental.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.repositories.BookingRepository;
import se.edugrade.carrental.repositories.CarRepository;
import se.edugrade.carrental.repositories.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BookingService implements BookingServiceInterface {


    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;


    public makeBooking(Long user_id, Long car_id, LocalDate startDate, LocalDate endDate){
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        Car car = carRepository.findById(car_id)
                .orElseThrow(() -> new RuntimeException("Car Not Found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(car);
        booking.setDateWhenPickedUp(startDate);
        booking.setDateWhenPickedUp(endDate);
        bookingRepository.save(booking);


    }





    @Override
    public int calculateTotalCost(LocalDate startDate, LocalDate endDate, int pricePerDay) {
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        return (int) (numberOfDays * pricePerDay);
    }


}


