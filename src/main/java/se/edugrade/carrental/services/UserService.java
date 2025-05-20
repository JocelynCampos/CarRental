package se.edugrade.carrental.services;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.exceptions.UserNotFoundException;
import se.edugrade.carrental.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Kamran Akbari
//lagt till loggning

@Service
public class UserService implements UserServiceInterface {

    private static final Logger adminLogger = LoggerFactory.getLogger("AdminLogger");

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public List<User> findAll() {
        adminLogger.info("ADMIN: Finding all users");
        return userRepository.findAll();
    }

    public User findById(Long id) {
        adminLogger.info("ADMIN: Finding user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    adminLogger.error("ADMIN: User with ID {} not found", id);
                    return new UserNotFoundException(id);
                });

    }


    public Boolean deleteById(Long id) {
        if (userRepository.existsById(id)) {
            adminLogger.info("ADMIN: Deleting user with ID: {}", id);
            userRepository.deleteById(id);
            return true;
        } else {
            adminLogger.warn("ADMIN: Delete failed - user with ID {} not found", id);
            throw new UserNotFoundException(id);
        }
    }


    public User findBySocialSecurityNumber(String socialSecurityNumber) {
        adminLogger.info("ADMIN: Searching user by SSN: {}", socialSecurityNumber);
        return userRepository.findBySocialSecurityNumber(socialSecurityNumber)
                .orElseThrow(() -> {
                    adminLogger.error("ADMIN: User with SSN {} not found", socialSecurityNumber);
                    return new UserNotFoundException("Användare med personnummer " + socialSecurityNumber + " hittades inte.");
                });
    }

    public User updateUserInfoBySSN(String socialSecurityNumber, User updatedUser) {
        adminLogger.info("ADMIN: Updating user info for SSN: {}", socialSecurityNumber);
        User existingUser = findBySocialSecurityNumber(socialSecurityNumber);

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setEmail(updatedUser.getEmail());
        adminLogger.info("ADMIN: Successfully updated user info for SSN: {}", socialSecurityNumber);

        return userRepository.save(existingUser);
    }

    public User save(User user) {
        adminLogger.info("ADMIN: Saving new user: {}", user);
        return userRepository.save(user);
    }
}





