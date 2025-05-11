package se.edugrade.carrental.services;

import se.edugrade.carrental.entities.User;

import java.util.List;

public interface UserServiceInterface
{
    User getUserById(Long id);
    User updateUser(Long id, User user);
    User saveUser(User user);
    void deleteUser(Long id);

    //Kamran Akbari
    User findBySocialSecurityNumber(String socialSecurityNumber);
    List<User> findAll();
    User findById(Long id);
    Boolean deleteById(Long id);
    User save(User user);
}
