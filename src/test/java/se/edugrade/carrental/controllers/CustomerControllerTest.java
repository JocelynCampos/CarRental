package se.edugrade.carrental.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.services.UserService;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

//Kamran Akbari

public class CustomerControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
public void setUp() {
    userService = mock(UserService.class);
    customerController = new CustomerController(userService);
    }

    @Test
    public void testUpdateCustomerInfo() {
        User updatedUser = new User();
        updatedUser.setFirstName("Updated");

        User savedUser = new User();
        savedUser.setFirstName("Updated");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("123456-7890");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(userService.updateUserInfoBySSN(eq("123456-7890"), eq(updatedUser))).thenReturn(savedUser);

        ResponseEntity<?> response = customerController.updateCustomerInfo(updatedUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(savedUser, response.getBody());
    }

    @Test
    public void testGetAllCustomers_ShouldReturnListOfUsers() {
        List<User> users = Arrays.asList(new User(), new User());

        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = customerController.getAllCustomers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetCustomerById_ShouldReturnUser() {
        User user = new User();
        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<User> response = customerController.getCustomerById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testDeleteCustomerById_ShouldReturnConfirmationMessage() {
        when(userService.deleteById(1L)).thenReturn(true);

        ResponseEntity<?> response = customerController.deleteCustomerById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("har raderats"));
    }

    @Test
    public void testAddCustomer() {
        User newUser = new User();
        User createdUser = new User();
        createdUser.setFirstName("Alice");

        when(userService.save(newUser)).thenReturn(createdUser);

        ResponseEntity<User> response = customerController.addCustomer(newUser);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(createdUser, response.getBody());
    }
}





