package se.edugrade.carrental.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



// Mohamed

@Service
public class UserService implements UserServiceInterface {

    private static final Logger adminLogger = LoggerFactory.getLogger("AdminLogger");

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long id) {return userRepository.findById(id).orElse(null);}

    @Override
    public User updateUser(Long id, User updatedUser) {
        adminLogger.info("Retrieving a user by ID: {}", id);
        User existingUser = userRepository.findById(id).orElseThrow();

        adminLogger.info("Updating user with ID: {}", id);
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());

        adminLogger.info("Update successful for user with ID: {}", id);
        return userRepository.save(existingUser);
    }

    @Override
    public User saveUser(User user) {
        adminLogger.info("Saving user with ID: {}", user.getId());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        adminLogger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }
}

