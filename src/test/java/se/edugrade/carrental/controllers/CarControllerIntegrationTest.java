package se.edugrade.carrental.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import se.edugrade.carrental.entities.Car;
import se.edugrade.carrental.repositories.CarRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Hugo Ransvi

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerIntegrationTest {

    @BeforeEach
    void clearDataBase() {
        carRepository.deleteAll();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddAndGetCar() throws Exception {
        Car car = new Car(400, "Kia", "EV9 GT", "ABC123", Car.CarStatus.FREE);

        String json = objectMapper.writeValueAsString(car);

        // POST
        mockMvc.perform(post("/api/v1/admin/addcar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // GET
        mockMvc.perform(get("/api/v1/admin/allcars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Kia"));
    }
}
