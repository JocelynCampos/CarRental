package se.edugrade.carrental.services;

import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.exceptions.UserNotFoundException;
import se.edugrade.carrental.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Mohamed

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setEmail(updatedUser.getEmail());

        return userRepository.save(existingUser);
    }


    @Override
    public User saveUser(User user) {

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }

                                // Kamran Akbari

    public List <User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }


    public Boolean deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            throw new UserNotFoundException(id);
        }
    }
    public User findBySocialSecurityNumber(String socialSecurityNumber) {
        return userRepository.findBySocialSecurityNumber(socialSecurityNumber)
                .orElseThrow(() -> new RuntimeException("User with Social Security Number" + socialSecurityNumber + " not found"));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}

