package se.edugrade.carrental.services;

import se.edugrade.carrental.entities.User;

import java.util.List;

public interface UserServiceInterface
{
    //Kamran Akbari
    User updateUserInfoBySSN(String socialSecurityNumber, User updatedUser);
    User findBySocialSecurityNumber(String socialSecurityNumber);
    List<User> findAll();
    User findById(Long id);
    Boolean deleteById(Long id);
    User save(User user);
}
