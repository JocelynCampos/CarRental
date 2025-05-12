package se.edugrade.carrental.services;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.repositories.BookingRepository;
import se.edugrade.carrental.repositories.CarRepository;
import se.edugrade.carrental.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import java.time.LocalDate;
import java.util.Optional;



@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CarRepository carRepository;

    @InjectMocks
    BookingService bookingService;

    @Test
    void createBooking_shouldReturnSavedValidBooking() {

        Long mockUserId = 1L;
        Long mockCarId = 1L;
        LocalDate startDate = LocalDate.of(2025, 5, 10);
        LocalDate endDate = LocalDate.of(2025, 5, 20);


        User mockUser = new User();
        mockUser.setId(mockUserId);

        Car mockCar = new Car();
        mockCar.setId(mockCarId);
        mockCar.setPricePerDay(500);

        Booking savedMockBooking = new Booking();
        savedMockBooking.setUser(mockUser);
        savedMockBooking.setCar(mockCar);
        savedMockBooking.setDateWhenPickedUp(startDate);
        savedMockBooking.setDateWhenTurnedIn(endDate);
        savedMockBooking.setTotalCost(5000);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        when(carRepository.findById(mockCarId)).thenReturn(Optional.of(mockCar));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedMockBooking);

        Booking resultIfMockWorks = bookingService.createBooking(mockUserId, mockCarId, startDate, endDate);

        assertNotNull(resultIfMockWorks);
        assertEquals(mockUser, resultIfMockWorks.getUser());
        assertEquals(mockCar, resultIfMockWorks.getCar());
        assertEquals(5000, resultIfMockWorks.getTotalCost());
        verify(userRepository).findById(mockUserId);
        verify(carRepository).findById(mockCarId);}
}


