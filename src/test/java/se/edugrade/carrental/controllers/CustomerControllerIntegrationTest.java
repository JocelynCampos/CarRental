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
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.repositories.BookingRepository;
import se.edugrade.carrental.repositories.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
 
// Kamran Akbari
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddAndGetCustomer() throws Exception {
        User user = new User();
        user.setFirstName("Elin");
        user.setLastName("Anderson");
        user.setEmail("elin@example.com");
        user.setAddress("Testgatan 1");
        user.setPhoneNumber("0701234567");
        user.setSocialSecurityNumber("19950505-7890");

        String json = objectMapper.writeValueAsString(user);


        //post /admin/addcustomer

        mockMvc.perform(post("/api/v1/admin/addcustomer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        //Get /admin/customers
        mockMvc.perform(get("/api/v1/admin/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Elin")));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteCustomer() throws Exception {
        User user = new User();
        user.setFirstName("Johan");
        user.setLastName("Karlsson");
        user.setEmail("johan@example.com");
        user.setAddress("Hjortgatan 1");
        user.setPhoneNumber("0733234567");
        user.setSocialSecurityNumber("18850505-7895");

        user = userRepository.save(user);


        //Delete /admin/removecustomer{id}
        mockMvc.perform(delete("/api/v1/admin/removecustomer/" + user.getId()))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "19950505-7890")
    void testUpdateCustomerInfo_Success() throws Exception {
        //  skapa och spara användare
        User user = new User();
        user.setFirstName("Elin");
        user.setLastName("Andersson");
        user.setEmail("elin@old.com");
        user.setAddress("Gammal gata");
        user.setPhoneNumber("0700000000");
        user.setSocialSecurityNumber("19950505-7890");

        userRepository.save(user);

        // Ny uppdaterad användar
        User updated = new User();
        updated.setFirstName("Elin");
        updated.setLastName("Andersson");
        updated.setEmail("elin@ny.com");
        updated.setAddress("Testgatan 1");
        updated.setPhoneNumber("0701234567");

        String json = objectMapper.writeValueAsString(updated);

        mockMvc.perform(put("/api/v1/updateinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("elin@ny.com")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetCustomerById_Success() throws Exception {
        // skapa och spara användare
        User user = new User();
        user.setFirstName("Maria");
        user.setLastName("Lindberg");
        user.setEmail("maria@example.com");
        user.setAddress("Storgatan 5");
        user.setPhoneNumber("0701122334");
        user.setSocialSecurityNumber("19751230-9101");

        user = userRepository.save(user);

        mockMvc.perform(get("/api/v1/admin/customer/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Maria")))
                .andExpect(jsonPath("$.email", is("maria@example.com")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetCustomerById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/admin/customer/99999"))
                .andExpect(status().isNotFound());
    }
}



