package se.edugrade.carrental.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.services.UserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

                     //Kamran Akbari

public class CustomerControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateCustomerInfo_UserNotFound_ReturnNotFound() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("1234567890");
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userService.findBySocialSecurityNumber("1234567890")).thenReturn(null);

        User update = new User();
        ResponseEntity<?> response = customerController.updateCustomerInfo(update);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void TestUpdateCustomerInfo_Success() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("1234567890");
        SecurityContextHolder.getContext().setAuthentication(auth);

        User existing = new User();
        existing.setId(1L);
        existing.setSocialSecurityNumber("1234567890");

        User update = new User();
        update.setFirstName("new");
        update.setLastName("Name");
        update.setAddress("New Address");
        update.setEmail("new@example.com");
        update.setPhoneNumber("070134567");

        when(userService.findBySocialSecurityNumber("1234567890")).thenReturn(existing);
        when(userService.save(any(User.class))).thenReturn(existing);

        ResponseEntity<?> response = customerController.updateCustomerInfo(update);
        User result = (User) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("new", result.getFirstName());
        assertEquals("new@example.com", result.getEmail());

    }

    @Test
    public void testGetAllCustomers_ReturnUserList() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = customerController.getAllCustomers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetCustomerById_NotFound() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<User> response = customerController.getCustomerById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetCustomerById_Success() {
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<User> response = customerController.getCustomerById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testDeleteCustomerById_NotFound() {
        when(userService.deleteById(1L)).thenReturn(false);

        ResponseEntity<?> response = customerController.deleteCustomerById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteCustomerById_Success() {
        when(userService.deleteById(1L)).thenReturn(true);

        ResponseEntity<?> response = customerController.deleteCustomerById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddCustomer_Success() {
        User input = new User();
        input.setFirstName("Test");

        User saved = new User();
        saved.setId(1L);
        saved.setFirstName("Test");

        when(userService.save(input)).thenReturn(saved);

        ResponseEntity<User> response = customerController.addCustomer(input);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saved, response.getBody());
    }
}



