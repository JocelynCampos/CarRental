package se.edugrade.carrental.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.services.UserService;

//Logiken flyttad till service och tagit bort NOT_Found då exception hanteras i service.

import java.util.List;
                      // Kamran Akbari
@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final UserService userService;

    public CustomerController(UserService userService) {
        this.userService = userService;
    }



    @PutMapping("/updateinfo")
    public ResponseEntity<?> updateCustomerInfo(@RequestBody User updatedUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String socialSecurityNumber = auth.getName();

        User savedUser = userService.updateUserInfoBySSN(socialSecurityNumber, updatedUser);
        return ResponseEntity.ok(savedUser);

    }

    @GetMapping("/admin/customers")
    public ResponseEntity<List<User>> getAllCustomers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping ("/admin/customer/{id}")
    public ResponseEntity<User> getCustomerById(@PathVariable long id) {
        User  user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping ("/admin/removecustomer/{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("Kunden med ID " + id + " har raderats.");
    }

    @PostMapping ("/admin/addcustomer")
    public ResponseEntity<User> addCustomer(@RequestBody User newUser)
    {
        User created = userService.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
