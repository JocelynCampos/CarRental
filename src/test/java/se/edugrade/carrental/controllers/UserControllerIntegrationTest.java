package se.edugrade.carrental.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.repositories.UserRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Skapat av Mohamed

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllCustomers() throws Exception {
        // Arrange
        User user1 = new User("19850101-9999", "Ali", "Mahmoud", "Address 1", "0700000001", "ali@test.com", List.of());
        User user2 = new User("19850202-8232", "Mona", "Ibrahim", "Address 2", "0700000002", "mona@test.com", List.of());
        userRepository.saveAll(List.of(user1, user2));


        mockMvc.perform(get("/api/v1/admin/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.socialSecurityNumber=='19850101-9999')]").exists())
                .andExpect(jsonPath("$[?(@.socialSecurityNumber=='19850202-8232')]").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getCustomerById() throws Exception {

        User user = new User("19881122-3456", "Sara", "Youssef", "Testgatan 5", "0701112222", "sara@test.com", List.of());
        user = userRepository.save(user);


        mockMvc.perform(get("/api/v1/admin/customer/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.socialSecurityNumber").value("19881122-3456"))
                .andExpect(jsonPath("$.firstName").value("Sara"))
                .andExpect(jsonPath("$.email").value("sara@test.com"));
    }
}
