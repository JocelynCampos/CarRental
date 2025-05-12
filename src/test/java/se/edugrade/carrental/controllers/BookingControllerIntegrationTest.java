package se.edugrade.carrental.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import se.edugrade.carrental.entities.Booking;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.repositories.BookingRepository;
import se.edugrade.carrental.repositories.CarRepository;
import se.edugrade.carrental.repositories.UserRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})

@SpringBootTest
@AutoConfigureMockMvc


public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    void getAdminActiveOrders_shouldReturn200OK() throws Exception {
        //simulate Create booking
        Booking booking = new Booking();
        booking.setDateWhenPickedUp(LocalDate.now().minusDays(1));
        booking.setDateWhenTurnedIn(LocalDate.now().plusDays(1));
        booking.setStatus(Booking.BookingStatus.ACTIVE);
        bookingRepository.save(booking);


        mockMvc.perform(get("/api/v1/admin/activeorders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

    }

    @Test
    void removeOrderAdmin_shouldReturn204OKAndDeleteBooking() throws Exception {

        User mockUser = new User();
        mockUser.setSocialSecurityNumber("19940408-0408");
        mockUser.setFirstName("Test");
        mockUser.setLastName("Testesson");
        mockUser.setEmail("test@test.com");
        mockUser.setPhoneNumber("070-1234567");
        mockUser.setAddress("Testvägen 1");
        mockUser = userRepository.save(mockUser);
        mockUser = userRepository.findById(mockUser.getId()).orElseThrow();


        Car car = new Car();
        car.setBrand("Mercedes-Benz");
        car.setModel("A180");
        car.setPricePerDay(200);
        car.setRegistrationNumber("ABC123");
        car.setStatus(Car.CarStatus.BOOKED);
        car = carRepository.save(car);
        car = carRepository.findById(car.getId()).orElseThrow();

        //skapa fake bokning
        Booking booking = new Booking();
        booking.setUser(mockUser);
        booking.setCar(car);
        booking.setDateWhenPickedUp(LocalDate.now().minusDays(5));
        booking.setDateWhenTurnedIn(LocalDate.now().plusDays(5));
        booking.setStatus(Booking.BookingStatus.ACTIVE);
        booking.setTotalCost(2000);
        booking = bookingRepository.save(booking);


        mockMvc.perform(delete("/api/v1/admin/removeorder")
                .param("bookingId", booking.getId().toString())
                .accept("application/json"))
                .andExpect(status().isNoContent());

        assertFalse(bookingRepository.findById(booking.getId()).isPresent());
    }


}
