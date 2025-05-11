package se.edugrade.carrental.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/*Erik Edman*/

@ExtendWith(MockitoExtension.class)
class StatisticsServiceUnitTest
{
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private StatisticsService service;
    private Booking bookingOne;
    private Booking bookingTwo;
    private Booking bookingThree;
    private User userOne;
    private User userTwo;
    private Car carOne;
    private Car carTwo;
    private Car carThree;

    @BeforeEach
    void setUp()
    {
        userOne = new User();
        userTwo = new User();

        userOne.setId(1L);
        userOne.setSocialSecurityNumber("19960618-1234");
        userOne.setFirstName("Erik");
        userOne.setLastName("Edman");
        userOne.setAddress(null);
        userOne.setPhoneNumber(null);
        userOne.setEmail(null);

        userTwo.setId(2L);
        userTwo.setSocialSecurityNumber("19880711-4567");
        userTwo.setFirstName("Olof");
        userTwo.setLastName("Olsson");
        userTwo.setAddress(null);
        userTwo.setPhoneNumber(null);
        userTwo.setEmail(null);

        carOne = new Car();
        carTwo = new Car();
        carThree = new Car();

        carOne.setId(1L);
        carOne.setPricePerDay(500);
        carOne.setBrand("Audi");
        carOne.setModel("A5");
        carOne.setRegistrationNumber("ABC123");
        carOne.setStatus(Car.CarStatus.BOOKED);

        carTwo.setId(2L);
        carTwo.setPricePerDay(450);
        carTwo.setBrand("Volvo");
        carTwo.setModel("V70");
        carTwo.setRegistrationNumber("ABC456");
        carTwo.setStatus(Car.CarStatus.BOOKED);

        carThree.setId(3L);
        carThree.setPricePerDay(600);
        carThree.setBrand("Audi");
        carThree.setModel("Q7");
        carThree.setRegistrationNumber("ABC789");
        carThree.setStatus(Car.CarStatus.BOOKED);

        bookingOne = new Booking();
        bookingTwo = new Booking();
        bookingThree = new Booking();

        bookingOne.setId(1L);
        bookingOne.setUser(userOne);
        bookingOne.setCar(carOne);
        bookingOne.setDateWhenPickedUp(LocalDate.of(2025, 1, 1));
        bookingOne.setDateWhenTurnedIn(LocalDate.of(2025, 1, 5));
        bookingOne.setTotalCost(2500);

        bookingTwo.setId(2L);
        bookingTwo.setUser(userTwo);
        bookingTwo.setCar(carTwo);
        bookingTwo.setDateWhenPickedUp(LocalDate.of(2025, 1, 2));
        bookingTwo.setDateWhenTurnedIn(LocalDate.of(2025, 1, 4));
        bookingTwo.setTotalCost(1350);

        bookingThree.setId(3L);
        bookingThree.setUser(userOne);
        bookingThree.setCar(carThree);
        bookingThree.setDateWhenPickedUp(LocalDate.of(2025, 3, 10));
        bookingThree.setDateWhenTurnedIn(LocalDate.of(2025, 3, 13));
        bookingThree.setTotalCost(2600);
    }

    @Test
    void getMostPopularPeriodShouldReturnMostPopularPeriod()
    {
        // bookingOne: Jan 1–5
        // bookingTwo: Jan 2–4 => overlap on Jan 2–4 (2 bookings)
        when(bookingService.findAllBookings()).thenReturn(List.of(bookingOne, bookingTwo));

        String result = service.getMostPopularPeriod();

        assertEquals("Most popular period: 2025-01-02 to 2025-01-04", result);
    }

    @Test
    void getMostPopularPeriodShouldThrowExceptionWhenNoBookingsExist()
    {
        when(bookingService.findAllBookings()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> service.getMostPopularPeriod());
    }

    @Test
    void getMostPopularPeriodShouldHandleSingleBooking()
    {
        // Only bookingOne: Jan 1–5
        when(bookingService.findAllBookings()).thenReturn(List.of(bookingOne));

        String result = service.getMostPopularPeriod();

        assertEquals("Most popular period: 2025-01-01 to 2025-01-05", result);
    }

    @Test
    void getMostPopularPeriodShouldReturnLongestMaxPeriodIfMultiple()
    {
        // bookingOne: Mar 10–13 (4 days overlap)
        // bookingTwo: Jan 1–3
        bookingOne.setDateWhenPickedUp(LocalDate.of(2025, 3, 10));
        bookingOne.setDateWhenTurnedIn(LocalDate.of(2025, 3, 13));

        bookingTwo.setDateWhenPickedUp(LocalDate.of(2025, 1, 1));
        bookingTwo.setDateWhenTurnedIn(LocalDate.of(2025, 1, 3));

        when(bookingService.findAllBookings()).thenReturn(List.of(bookingOne, bookingTwo));

        String result = service.getMostPopularPeriod();

        assertEquals("Most popular period: 2025-03-10 to 2025-03-13", result);
    }

    @Test
    void getMostPopularPeriodShouldHandleGapsInPopularDates()
    {
        // bookingOne: Jan 1–1
        // bookingTwo: Jan 3–3 => max on 1st and 3rd, but not consecutive
        bookingOne.setDateWhenPickedUp(LocalDate.of(2025, 1, 1));
        bookingOne.setDateWhenTurnedIn(LocalDate.of(2025, 1, 1));

        bookingTwo.setDateWhenPickedUp(LocalDate.of(2025, 1, 3));
        bookingTwo.setDateWhenTurnedIn(LocalDate.of(2025, 1, 3));

        when(bookingService.findAllBookings()).thenReturn(List.of(bookingOne, bookingTwo));

        String result = service.getMostPopularPeriod();

        // Should pick either 1st or 3rd, but by sorting, picks 2025-01-01
        assertEquals("Most popular period: 2025-01-01 to 2025-01-01", result);
    }


    @Test
    void getMostRentedBrandShouldReturnMostRentedBrand()
    {
        when(bookingService.findAllBookings()).thenReturn(List.of(bookingOne, bookingTwo, bookingThree));

        String brand = service.getMostRentedBrand();

        assertEquals("Audi", brand);
    }

    @Test
    void getCarRentalCountsShouldReturnAccurateCounts()
    {
        //Lägger till en extra bookingOne för att testa att första bilen dyker upp 2 gånger.
        when(bookingService.findAllBookings()).thenReturn(List.of(bookingOne, bookingTwo, bookingThree, bookingOne));

        Map<String, Integer> result = service.getCarRentalCounts();

        assertEquals(2, result.get("ABC123"));
        assertEquals(1, result.get("ABC456"));
        assertEquals(1, result.get("ABC789"));
        assertEquals(3, result.size());
    }

    @Test
    void getAverageBookingCostShouldReturnAverageCost()
    {
        when(bookingService.findAllBookings()).thenReturn(List.of(bookingOne, bookingTwo, bookingThree));

        //(2600 + 1350 + 2500) / 3 = 2150
        double result = service.getAverageBookingCost();

        assertEquals(2150, result);
    }

    @Test
    void getRevenuePerCarShouldReturnRevenuePerCar()
    {
        when(bookingService.findAllBookings()).thenReturn(List.of(bookingOne, bookingTwo, bookingThree));

        Map<String, Integer> result = service.getRevenuePerCar();

        assertEquals(2500, result.get("ABC123"));
        assertEquals(1350, result.get("ABC456"));
        assertEquals(2600, result.get("ABC789"));
        assertEquals(3, result.size());
    }

    @Test
    void getTotalRevenueShouldReturnTotalRevenue()
    {
        when(bookingService.findAllBookings()).thenReturn(List.of(bookingOne, bookingTwo, bookingThree));

        Integer result = service.getTotalRevenue();

        assertEquals(6450, result);
    }
}