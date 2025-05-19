package se.edugrade.carrental.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.services.UserService;


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

        User existingUser = userService.findBySocialSecurityNumber(socialSecurityNumber);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setEmail(updatedUser.getEmail());
        // (ej personnumret)


        return ResponseEntity.ok(userService.save(existingUser));
    }

    @GetMapping("/admin/customers")
    public ResponseEntity<List<User>> getAllCustomers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping ("/admin/customer/{id}")
    public ResponseEntity<User> getCustomerById(@PathVariable long id) {
        User  user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/admin/removecustomer/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Long id) {
      userService.deleteUser(id);
      return ResponseEntity.noContent().build();
    }

    @PostMapping ("/admin/addcustomer")
    public ResponseEntity<User> addCustomer(@RequestBody User newUser)
    {
        User created = userService.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
