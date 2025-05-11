package se.edugrade.carrental.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCars_returnsFreeCarsOnly() throws Exception {
        mockMvc.perform(get("/api/v1/admin/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].status").value(org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is("FREE"))));
    }

    @Test
    void addCar_createsNewCar() throws Exception {
        Car car = new Car(300, "Mitsubishi", "Space Star", "ABC123", Car.CarStatus.FREE);
        String json = objectMapper.writeValueAsString(car);

        mockMvc.perform(post("/api/v1/admin/addcar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Mitsubishi"));
    }

    @Test
    void updateCar_updatesExistingCar() throws Exception {
        Car car = new Car(300, "Mitsubishi", "Space Star", "ABC123", Car.CarStatus.FREE);
        Car saved = carRepository.save(car);
        saved.setBrand("Ferrari");
        String json = objectMapper.writeValueAsString(saved);

        mockMvc.perform(put("/api/v1/admin/updatecar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Ferrari"));
    }
}
