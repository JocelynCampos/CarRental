package se.edugrade.carrental.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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


    public makeBooking(Long user_id, Long car_id, LocalDate ){

    }





    @Override
    public int calculateTotalCost(LocalDate startDate, LocalDate endDate, int pricePerDay) {
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        return (int) (numberOfDays * pricePerDay);
    }


}


