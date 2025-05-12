package se.edugrade.carrental.services;


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

import static java.nio.file.Files.delete;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
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
        verify(carRepository).findById(mockCarId);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void cancelUserBooking_ThrowExceptionIfBookingIsPastToday() {
        Long bookingId = 3L;
        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId);
        mockBooking.setDateWhenTurnedIn(LocalDate.now().minusDays(1));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(mockBooking));

        assertThrows(IllegalStateException.class, () -> bookingService.cancelUserBooking(bookingId)
        );

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).delete(any());
    }

    @Test
    void getUserExpiredBookingsTest() {
        Long mockUserId = 3L;

        User mockUser = new User();
        mockUser.setId(mockUserId);

        Booking expiredBooking = new Booking();
        expiredBooking.setUser(mockUser);
        expiredBooking.setDateWhenTurnedIn(LocalDate.now().minusDays(2));

        Booking futureBoooking = new Booking();
        futureBoooking.setUser(mockUser);
        futureBoooking.setDateWhenTurnedIn(LocalDate.now().plusDays(2));

        List<Booking> allBookings = List.of(expiredBooking,futureBoooking);

        when(bookingRepository.findAll()).thenReturn(allBookings);

        List<Booking> result = bookingService.getUserExpiredBookings(mockUserId);
        assertEquals(1, result.size());
        assertEquals(expiredBooking, result.get(0));

        verify(bookingRepository).findAll();
    }

    @Test
    void getUserActiveOrdersTest() {
        Long mockUserId = 4L;

        User mockUser = new User();
        mockUser.setId(mockUserId);

        Booking activeBooking = new Booking();
        activeBooking.setUser(mockUser);
        activeBooking.setStatus(Booking.BookingStatus.ACTIVE);
        activeBooking.setDateWhenPickedUp(LocalDate.now().minusDays(2));
        activeBooking.setDateWhenTurnedIn(LocalDate.now().plusDays(2));


        Booking inactiveBooking = new Booking();
        inactiveBooking.setUser(mockUser);
        inactiveBooking.setStatus(Booking.BookingStatus.COMPLETED);
        inactiveBooking.setDateWhenPickedUp(LocalDate.now().minusDays(4));
        inactiveBooking.setDateWhenTurnedIn(LocalDate.now().plusDays(1));

        List<Booking> allBookings = List.of(activeBooking,inactiveBooking);
        when(bookingRepository.findAll()).thenReturn(allBookings);
        List<Booking> result = bookingService.getUserActiveOrders(mockUserId);
        assertEquals(1, result.size());
        assertEquals(activeBooking, result.get(0));
        verify(bookingRepository).findAll();

    }


}


