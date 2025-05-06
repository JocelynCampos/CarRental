package se.edugrade.carrental.services;

import se.edugrade.carrental.entities.User;

public interface UserServiceInterface
{
    User getUserById(Long id);
    User updateUser(Long id, User user);
    User saveUser(User user);
    void deleteUser(Long id);
}
